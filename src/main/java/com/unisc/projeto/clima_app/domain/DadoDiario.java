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
	private Integer cdClima;
	
	public DadoDiario() {
	}

	public DadoDiario(Integer idDadoDiario, Localizacao localizacao, LocalDate data, Double temperaturaMax,
			Double temperaturaMin, Double precipitacaoTotal, Double velocidadeVentoMax, Integer cdClima) {
		super();
		this.idDadoDiario = idDadoDiario;
		this.localizacao = localizacao;
		this.data = data;
		this.temperaturaMax = temperaturaMax;
		this.temperaturaMin = temperaturaMin;
		this.precipitacaoTotal = precipitacaoTotal;
		this.velocidadeVentoMax = velocidadeVentoMax;
		this.cdClima = cdClima;
	}

	public DadoDiario(Localizacao localizacao, LocalDate data, Double temperaturaMax, Double temperaturaMin,
			Double precipitacaoTotal, Double velocidadeVentoMax, Integer cdClima) {
		super();
		this.localizacao = localizacao;
		this.data = data;
		this.temperaturaMax = temperaturaMax;
		this.temperaturaMin = temperaturaMin;
		this.precipitacaoTotal = precipitacaoTotal;
		this.velocidadeVentoMax = velocidadeVentoMax;
		this.cdClima = cdClima;
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

	public Integer getCdClima() {
		return cdClima;
	}

	public void setCdClima(Integer cdClima) {
		this.cdClima = cdClima;
	}

}
