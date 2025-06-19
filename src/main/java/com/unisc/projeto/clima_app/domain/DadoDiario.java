package com.unisc.projeto.clima_app.domain;

import java.time.LocalDate;

public class DadoDiario {
	private Integer idDadoDiario;
	private Localizacao localizacao;
	private LocalDate data;
	private Double temperaturaMax;
	private Double temperaturaMin;
	private Double precipitacaoTotal;
	private Double velocidadeVentoMax;

	public DadoDiario() {
		super();
	}

	public DadoDiario(Integer idDadoDiario, Localizacao localizacao, LocalDate data, Double temperaturaMax,
			Double temperaturaMin, Double precipitacaoTotal, Double velocidadeVentoMax) {
		super();
		this.idDadoDiario = idDadoDiario;
		this.localizacao = localizacao;
		this.data = data;
		this.temperaturaMax = temperaturaMax;
		this.temperaturaMin = temperaturaMin;
		this.precipitacaoTotal = precipitacaoTotal;
		this.velocidadeVentoMax = velocidadeVentoMax;
	}

	public DadoDiario(Localizacao localizacao, LocalDate data, Double temperaturaMax, Double temperaturaMin,
			Double precipitacaoTotal, Double velocidadeVentoMax) {
		super();
		this.localizacao = localizacao;
		this.data = data;
		this.temperaturaMax = temperaturaMax;
		this.temperaturaMin = temperaturaMin;
		this.precipitacaoTotal = precipitacaoTotal;
		this.velocidadeVentoMax = velocidadeVentoMax;
	}

	public Integer getIdDadoDiario() {
		return idDadoDiario;
	}

	public void setIdDadoDiario(Integer idDadoDiario) {
		this.idDadoDiario = idDadoDiario;
	}

	public Localizacao getLocalizacao() {
		return localizacao;
	}

	public void setLocalizacao(Localizacao localizacao) {
		this.localizacao = localizacao;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public Double getTemperaturaMax() {
		return temperaturaMax;
	}

	public void setTemperaturaMax(Double temperaturaMax) {
		this.temperaturaMax = temperaturaMax;
	}

	public Double getTemperaturaMin() {
		return temperaturaMin;
	}

	public void setTemperaturaMin(Double temperaturaMin) {
		this.temperaturaMin = temperaturaMin;
	}

	public Double getPrecipitacaoTotal() {
		return precipitacaoTotal;
	}

	public void setPrecipitacaoTotal(Double precipitacaoTotal) {
		this.precipitacaoTotal = precipitacaoTotal;
	}

	public Double getVelocidadeVentoMax() {
		return velocidadeVentoMax;
	}

	public void setVelocidadeVentoMax(Double velocidadeVentoMax) {
		this.velocidadeVentoMax = velocidadeVentoMax;
	}

}
