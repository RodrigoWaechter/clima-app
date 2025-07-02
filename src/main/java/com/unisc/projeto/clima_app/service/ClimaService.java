package com.unisc.projeto.clima_app.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.unisc.projeto.clima_app.api.GeocodingAPI;
import com.unisc.projeto.clima_app.api.OpenMeteoAPI;
import com.unisc.projeto.clima_app.dao.DadoDiarioDAO;
import com.unisc.projeto.clima_app.dao.DadoHorarioDAO;
import com.unisc.projeto.clima_app.dao.LocalizacaoDAO;
import com.unisc.projeto.clima_app.domain.ClimaInfoDTO;
import com.unisc.projeto.clima_app.domain.DadoDiario;
import com.unisc.projeto.clima_app.domain.DadoHorario;
import com.unisc.projeto.clima_app.domain.Localizacao;

public class ClimaService {
	private static final Logger LOGGER = Logger.getLogger(ClimaService.class.getName());

	private final LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO();
	private final DadoHorarioDAO dadoHorarioDAO = new DadoHorarioDAO();
	private final DadoDiarioDAO dadoDiarioDAO = new DadoDiarioDAO();
	private final GeocodingAPI geocodingAPI = new GeocodingAPI();
	private final OpenMeteoAPI openMeteoAPI = new OpenMeteoAPI();

	// unico metodo publico, retorna todos os dados climaticos
	public ClimaInfoDTO getDadosClimaticos(String nomeCidade) throws Exception {
		Localizacao localizacao = getLocalizacao(nomeCidade);

		Optional<DadoHorario> ultimoDadoHorario = dadoHorarioDAO.queryHoraAutal(localizacao.getIdLocalizacao());

		boolean dadosRecentesExistem = ultimoDadoHorario.isPresent()
				&& ultimoDadoHorario.get().getHorario().isAfter(LocalDateTime.now().minusHours(1));

		if (dadosRecentesExistem) {
			LOGGER.info("Dados recentes encontrados no banco para " + nomeCidade + ". ");
			return carregarDadosDoBanco(localizacao);
		} else {
			LOGGER.info("Dados desatualizados ou inexistentes para " + nomeCidade + ". Buscando na API.");

			try {
				return atualizaDadosApi(localizacao);
			} catch (Exception apiException) {
				LOGGER.severe("Falha ao buscar dados na API: " + apiException.getMessage());

				if (ultimoDadoHorario.isPresent()) {
					LOGGER.warning("API falhou. Retornando dados antigos do banco para " + nomeCidade);
					return carregarDadosDoBanco(localizacao);
				}

				throw new RuntimeException("Falha ao obter dados da API e não há dados no banco para: " + nomeCidade, apiException);
			}
		}
	}

	private ClimaInfoDTO carregarDadosDoBanco(Localizacao localizacao) {
		int id = localizacao.getIdLocalizacao();
		DadoHorario dadoAtual = dadoHorarioDAO.queryHoraAutal(id)
				.orElseThrow(() -> new RuntimeException("Dado atual não encontrado no banco."));
		List<DadoDiario> previsaoDiaria = dadoDiarioDAO.queryProximos7Dias(id);
		List<DadoHorario> previsaoHoraria = dadoHorarioDAO.queryProximas24Horas(id);

		return new ClimaInfoDTO(localizacao, dadoAtual, previsaoDiaria, previsaoHoraria);
	}

	// primeiro tenta pegar a localizaçao informada do banco, se nao, pega da API
	private Localizacao getLocalizacao(String nomeCidade) throws Exception {
		return localizacaoDAO.findByName(nomeCidade).orElseGet(() -> {
			return geocodingAPI.queryLocalizacaoPorNome(nomeCidade).map(localizacaoDAO::save)
					.orElseThrow(() -> new RuntimeException("Cidade não encontrada: " + nomeCidade));
		});
	}

	// pega os dados da API, salva no banco e depois retorna
	private ClimaInfoDTO atualizaDadosApi(Localizacao localizacao) throws Exception {
		Optional<DadoHorario> dadoAtualOpt = openMeteoAPI.queryClimaAtualFromJSON(localizacao);
		List<DadoDiario> previsaoDiaria = openMeteoAPI.queryPrevisaoDiariaFromJSON(localizacao);
		List<DadoHorario> previsaoHorariaCompleta = openMeteoAPI.queryPrevisaoHorariaFromJSON(localizacao);

		if (dadoAtualOpt.isEmpty() || previsaoDiaria.isEmpty()) {
			throw new RuntimeException("API retornou dados incompletos ou vazios.");
		}
        
        DadoHorario dadoHorarioAtual = dadoAtualOpt.get();

        previsaoHorariaCompleta.add(dadoHorarioAtual);

        // Usa um Map para remover automaticamente os registos duplicados,
        // mantendo o último que foi adicionado (o mais recente).
        Map<LocalDateTime, DadoHorario> dadosUnicosMap = new LinkedHashMap<>();
        for (DadoHorario dado : previsaoHorariaCompleta) {
            dadosUnicosMap.put(dado.getHorario(), dado);
        }
        List<DadoHorario> dadosHorariosUnicos = new ArrayList<>(dadosUnicosMap.values());

		// salva a previsao diaria e horaria
		dadoDiarioDAO.save(previsaoDiaria);
		dadoHorarioDAO.save(dadosHorariosUnicos); // Salva a lista já sem duplicados
		LOGGER.info("Dados da API para '" + localizacao.getNomeCidade() + "' salvos no banco de dados.");

		// Filtra a lista completa de previsão horária para conter apenas as próximas 24h
		LocalDateTime agora = LocalDateTime.now();
		List<DadoHorario> proximas24Horas = dadosHorariosUnicos.stream()
				.filter(dado -> !dado.getHorario().isBefore(agora.withMinute(0).withSecond(0).withNano(0))).limit(24)
				.collect(Collectors.toList());

		return new ClimaInfoDTO(localizacao, dadoHorarioAtual, previsaoDiaria, proximas24Horas);
	}
}
