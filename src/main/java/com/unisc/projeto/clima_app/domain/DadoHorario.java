package com.unisc.projeto.clima_app.domain;

import java.time.LocalDateTime;

public class DadoHorario {

	private Integer idDadoHorario;
	private Localizacao localizacao;
	private LocalDateTime horario;
	private Double temperatura;
	private Double umidadeRelativa;
	private Double sensacaoTermica;
	private Double velocidadeVento;
	private Short direcaoVento;
	private Double precipitacao;
	private Integer cdClima;
	
	public DadoHorario() {
	}

	public DadoHorario(Integer idDadoHorario, Localizacao localizacao, LocalDateTime horario, Double temperatura,
			Double umidadeRelativa, Double sensacaoTermica, Double velocidadeVento, Short direcaoVento,
			Double precipitacao, Integer cdClima) {
		this.idDadoHorario = idDadoHorario;
		this.localizacao = localizacao;
		this.horario = horario;
		this.temperatura = temperatura;
		this.umidadeRelativa = umidadeRelativa;
		this.sensacaoTermica = sensacaoTermica;
		this.velocidadeVento = velocidadeVento;
		this.direcaoVento = direcaoVento;
		this.precipitacao = precipitacao;
		this.cdClima = cdClima;
	}

	public DadoHorario(Localizacao localizacao, LocalDateTime horario, Double temperatura, Double umidadeRelativa,
			Double sensacaoTermica, Double velocidadeVento, Short direcaoVento, Double precipitacao, Integer cdClima) {
		this.localizacao = localizacao;
		this.horario = horario;
		this.temperatura = temperatura;
		this.umidadeRelativa = umidadeRelativa;
		this.sensacaoTermica = sensacaoTermica;
		this.velocidadeVento = velocidadeVento;
		this.direcaoVento = direcaoVento;
		this.precipitacao = precipitacao;
		this.cdClima = cdClima;
	}

	public Integer getIdDadoHorario() {
		return idDadoHorario;
	}

	public void setIdDadoHorario(Integer id) {
		this.idDadoHorario = id;
	}

	public Localizacao getLocalizacao() {
		return localizacao;
	}

	public void setLocalizacao(Localizacao localizacao) {
		this.localizacao = localizacao;
	}

	public LocalDateTime getHorario() {
		return horario;
	}

	public void setHorario(LocalDateTime horario) {
		this.horario = horario;
	}

	public Double getTemperatura() {
		return temperatura;
	}

	public void setTemperatura(Double temperatura) {
		this.temperatura = temperatura;
	}

	public Double getUmidadeRelativa() {
		return umidadeRelativa;
	}

	public void setUmidadeRelativa(Double umidadeRelativa) {
		this.umidadeRelativa = umidadeRelativa;
	}

	public Double getSensacaoTermica() {
		return sensacaoTermica;
	}

	public void setSensacaoTermica(Double sensacaoTermica) {
		this.sensacaoTermica = sensacaoTermica;
	}

	public Double getVelocidadeVento() {
		return velocidadeVento;
	}

	public void setVelocidadeVento(Double velocidadeVento) {
		this.velocidadeVento = velocidadeVento;
	}

	public Short getDirecaoVento() {
		return direcaoVento;
	}

	public void setDirecaoVento(Short direcaoVento) {
		this.direcaoVento = direcaoVento;
	}

	public Double getPrecipitacao() {
		return precipitacao;
	}

	public void setPrecipitacao(Double precipitacao) {
		this.precipitacao = precipitacao;
	}

	public Integer getCdClima() {
		return cdClima;
	}

	public void setCdClima(Integer cdClima) {
		this.cdClima = cdClima;
	}

}
