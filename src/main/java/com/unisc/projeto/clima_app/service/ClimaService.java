package com.unisc.projeto.clima_app.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

	private final LocalizacaoDAO localizacaoDAO = new LocalizacaoDAO();
	private final DadoHorarioDAO dadoHorarioDAO = new DadoHorarioDAO();
	private final DadoDiarioDAO dadoDiarioDAO = new DadoDiarioDAO();
	private final GeocodingAPI geocodingAPI = new GeocodingAPI();
	private final OpenMeteoAPI openMeteoAPI = new OpenMeteoAPI();

	// unico metodo publico, retorna todos os dados climaticos
	public ClimaInfoDTO getDadosClimaticos(String nomeCidade) throws Exception {
		Localizacao localizacao = getLocalizacao(nomeCidade);

		Optional<ClimaInfoDTO> infoAtualizada = atualizaDadosApi(localizacao);

		if (infoAtualizada.isPresent()) {
			return infoAtualizada.get();
		}
		throw new RuntimeException("Erro com a API, dados climaticos não encontrados.");

	}

	// primeiro tenta pegar a localizaçao informada do banco, se nao, pega da API
	private Localizacao getLocalizacao(String nomeCidade) throws Exception {
		return localizacaoDAO.findByName(nomeCidade).orElseGet(() -> {
			return geocodingAPI.queryLocalizacaoPorNome(nomeCidade).map(localizacaoDAO::save)
					.orElseThrow(() -> new RuntimeException("Cidade não encontrada: " + nomeCidade));
		});
	}

	// pega os dados da API, salva no banco e depois retorna
	private Optional<ClimaInfoDTO> atualizaDadosApi(Localizacao localizacao) {
		try {
			Optional<DadoHorario> dadoAtualOpt = openMeteoAPI.queryClimaAtualFromJSON(localizacao);
			List<DadoDiario> previsaoDiaria = openMeteoAPI.queryPrevisaoDiariaFromJSON(localizacao);
			List<DadoHorario> previsaoHorariaCompleta = openMeteoAPI.queryPrevisaoHorariaFromJSON(localizacao);

			if (dadoAtualOpt.isEmpty() || previsaoDiaria.isEmpty() || previsaoHorariaCompleta.isEmpty()) {
				return Optional.empty();
			}

			// Filtra a lista completa de previsão horária para conter apenas as próximas
			// 24h
			LocalDateTime agora = LocalDateTime.now();
			List<DadoHorario> proximas24Horas = previsaoHorariaCompleta.stream()
					.filter(dado -> !dado.getHorario().isBefore(agora.withMinute(0).withSecond(0).withNano(0)))
					.limit(24).collect(Collectors.toList());

			DadoHorario dadoAtual = dadoAtualOpt.get();

			salvarPrevisoes(dadoAtual, previsaoDiaria, previsaoHorariaCompleta);

			return Optional.of(new ClimaInfoDTO(localizacao, dadoAtual, previsaoDiaria, proximas24Horas));

		} catch (Exception e) {
			return Optional.empty();
		}
	}

	// salva a previsao diaria e horaria
	private void salvarPrevisoes(DadoHorario dadoAtual, List<DadoDiario> previsaoDiaria,
			List<DadoHorario> previsaoHoraria) {
		try {
			List<DadoHorario> todosDadosHorarios = new ArrayList<>();
			todosDadosHorarios.add(dadoAtual);
			todosDadosHorarios.addAll(previsaoHoraria);

			dadoDiarioDAO.save(previsaoDiaria);
			dadoHorarioDAO.save(todosDadosHorarios);
		} catch (Exception e) {
			throw new RuntimeException("Falha ao persistir dados da API", e);
		}
	}
}