package com.unisc.projeto.clima_app.domain;

import java.util.List;

public class ClimaInfoDTO {
	private final Localizacao localizacao;
	private final DadoHorario dadoHorarioAtual;
	private final List<DadoDiario> previsaoDiaria;
	private final List<DadoHorario> previsaoHoraria;

	public ClimaInfoDTO(Localizacao localizacao, DadoHorario dadoHorarioAtual, List<DadoDiario> previsaoDiaria,
			List<DadoHorario> previsaoHoraria) {
		this.localizacao = localizacao;
		this.dadoHorarioAtual = dadoHorarioAtual;
		this.previsaoDiaria = previsaoDiaria;
		this.previsaoHoraria = previsaoHoraria;
	}

	public Localizacao getLocalizacao() {
		return localizacao;
	}

	public DadoHorario getDadoHorarioAtual() {
		return dadoHorarioAtual;
	}

	public List<DadoDiario> getPrevisaoDiaria() {
		return previsaoDiaria;
	}

	public List<DadoHorario> getPrevisaoHoraria() {
		return previsaoHoraria;
	}
}