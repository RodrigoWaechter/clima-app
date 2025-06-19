package com.unisc.projeto.clima_app.domain;

import java.time.LocalDateTime;

public class Localizacao {

	private Integer idLocalizacao;
	private String nomeCidade;
	private Double latitude;
	private Double longitude;
	private LocalDateTime dataHoraRegistro;

	public Localizacao() {
	}

	public Localizacao(Integer idLocalizacao, String nomeCidade, Double latitude, Double longitude,
			LocalDateTime dataHoraRegistro) {
		this.idLocalizacao = idLocalizacao;
		this.nomeCidade = nomeCidade;
		this.latitude = latitude;
		this.longitude = longitude;
		this.dataHoraRegistro = dataHoraRegistro;
	}

	public Localizacao(String nomeCidade, Double latitude, Double longitude, LocalDateTime dataHoraRegistro) {
		this.nomeCidade = nomeCidade;
		this.latitude = latitude;
		this.longitude = longitude;
		this.dataHoraRegistro = dataHoraRegistro;
	}


	public Integer getIdLocalizacao() {
		return idLocalizacao;
	}

	public void setIdLocalizacao(Integer idLocalizacao) {
		this.idLocalizacao = idLocalizacao;
	}

	public String getNomeCidade() {
		return nomeCidade;
	}

	public void setNomeCidade(String nomeCidade) {
		this.nomeCidade = nomeCidade;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public LocalDateTime getDataHoraRegistro() {
		return dataHoraRegistro;
	}

	public void setDataHoraRegistro(LocalDateTime dataHoraRegistro) {
		this.dataHoraRegistro = dataHoraRegistro;
	}

	@Override
	public String toString() {
		return "Localizacao{" + "id=" + idLocalizacao + ", nomeCidade='" + nomeCidade + '\'' + ", latitude=" + latitude
				+ ", longitude=" + longitude + ", dataHoraRegistro=" + dataHoraRegistro + '}';
	}
}
