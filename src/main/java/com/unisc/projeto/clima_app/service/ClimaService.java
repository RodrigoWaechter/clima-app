package com.unisc.projeto.clima_app.service;

import java.util.List;
import java.util.Optional;

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

   //unico metodo publico, retorna todos os dados climaticos
    public ClimaInfoDTO getDadosClimaticos(String nomeCidade) throws Exception {
        Localizacao localizacao = getLocalizacao(nomeCidade);

        Optional<ClimaInfoDTO> infoAtualizada = atualizaDadosApi(localizacao);
        
        if (infoAtualizada.isPresent()) {
            return infoAtualizada.get();
        }

        return carregarDoBanco(localizacao)
                .orElseThrow(() -> new Exception("Falha ao obter dados da API e não há dados de fallback no banco para " + nomeCidade));
    }

    //primeiro tenta pegar a localizaçao informada do banco, se nao, pega da API
    private Localizacao getLocalizacao(String nomeCidade) throws Exception {
        return localizacaoDAO.findByName(nomeCidade)
                .orElseGet(() -> {
                    return geocodingAPI.queryLocalizacaoPorNome(nomeCidade)
                            .map(localizacaoDAO::save)
                            .orElseThrow(() -> new RuntimeException("Cidade não encontrada: " + nomeCidade));
                });
    }

   //pega os dados da API, salva no banco e depois retorna
    private Optional<ClimaInfoDTO> atualizaDadosApi(Localizacao localizacao) {
        try {
            Optional<DadoHorario> dadoAtualOpt = openMeteoAPI.queryClimaAtualFromJSON(localizacao);
            List<DadoDiario> previsaoDiaria = openMeteoAPI.queryPrevisaoDiariaFromJSON(localizacao);
            List<DadoHorario> previsaoHoraria = openMeteoAPI.queryPrevisaoHorariaFromJSON(localizacao);
            
            if (dadoAtualOpt.isEmpty() || previsaoDiaria.isEmpty() || previsaoHoraria.isEmpty()) {
                return Optional.empty();
            }
            
            salvarPrevisoes(previsaoDiaria, previsaoHoraria);
            
            return carregarDoBanco(localizacao);

        } catch (Exception e) {
            return Optional.empty();
        }
    }

    
    //carrega as informaçoes climaticas salvas no banco
    private Optional<ClimaInfoDTO> carregarDoBanco(Localizacao localizacao) {
        Optional<DadoHorario> dadoAtual = dadoHorarioDAO.queryHoraAutal(localizacao.getIdLocalizacao());
        if (dadoAtual.isEmpty()) {
            return Optional.empty();
        }
        
        List<DadoDiario> previsaoDiaria = dadoDiarioDAO.queryProximos7Dias(localizacao.getIdLocalizacao());
        List<DadoHorario> previsaoHoraria = dadoHorarioDAO.queryProximas24Horas(localizacao.getIdLocalizacao());
        
        return Optional.of(new ClimaInfoDTO(localizacao, dadoAtual.get(), previsaoDiaria, previsaoHoraria));
    }
    
    //salva a previsao diaria e horaria
    private void salvarPrevisoes(List<DadoDiario> previsaoDiaria, List<DadoHorario> previsaoHoraria) {
        try {
            dadoDiarioDAO.save(previsaoDiaria);
            dadoHorarioDAO.save(previsaoHoraria);
        } catch (Exception e) {
            throw new RuntimeException("Falha ao persistir dados da API", e);
        }
    }
}