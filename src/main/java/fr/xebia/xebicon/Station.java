package fr.xebia.xebicon;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Station {
    private Long gid;
    private String numstat;
    private String ident;
    private String adresse;
    private String commune;
    private String dateserv;
    private String ligncorr;
    private String nbsuppor;
    private String nom;
    private String tarif;
    private String termbanc;
    private String typea;
    private String geom;
    private String geom_err;
    private Long geom_o;
    private String cdate;
    private String mdate;

    @JsonCreator
    public Station(
            @JsonProperty(value = "GID") Long gid,
            @JsonProperty(value = "NUMSTAT") String numstat,
            @JsonProperty(value = "IDENT") String ident,
            @JsonProperty(value = "ADRESSE") String adresse,
            @JsonProperty(value = "COMMUNE") String commune,
            @JsonProperty(value = "DATESERV") String dateserv,
            @JsonProperty(value = "LIGNCORR") String ligncorr,
            @JsonProperty(value = "NBSUPPOR") String nbsuppor,
            @JsonProperty(value = "NOM") String nom,
            @JsonProperty(value = "TARIF") String tarif,
            @JsonProperty(value = "TERMBANC") String termbanc,
            @JsonProperty(value = "TYPEA") String typea,
            @JsonProperty(value = "GEOM") String geom,
            @JsonProperty(value = "GEOM_ERR") String geom_err,
            @JsonProperty(value = "GEOM_O") Long geom_o,
            @JsonProperty(value = "CDATE") String cdate,
            @JsonProperty(value = "MDATE") String mdate
    ) {
        this.gid = gid;
        this.numstat = numstat;
        this.ident = ident;
        this.adresse = adresse;
        this.commune = commune;
        this.dateserv = dateserv;
        this.ligncorr = ligncorr;
        this.nbsuppor = nbsuppor;
        this.nom = nom;
        this.tarif = tarif;
        this.termbanc = termbanc;
        this.typea = typea;
        this.geom = geom;
        this.geom_err = geom_err;
        this.geom_o = geom_o;
        this.cdate = cdate;
        this.mdate = mdate;
    }

    public Long getGid() {
        return gid;
    }

    public void setGid(Long gid) {
        this.gid = gid;
    }

    public String getNumstat() {
        return numstat;
    }

    public void setNumstat(String numstat) {
        this.numstat = numstat;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public String getDateserv() {
        return dateserv;
    }

    public void setDateserv(String dateserv) {
        this.dateserv = dateserv;
    }

    public String getLigncorr() {
        return ligncorr;
    }

    public void setLigncorr(String ligncorr) {
        this.ligncorr = ligncorr;
    }

    public String getNbsuppor() {
        return nbsuppor;
    }

    public void setNbsuppor(String nbsuppor) {
        this.nbsuppor = nbsuppor;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getTarif() {
        return tarif;
    }

    public void setTarif(String tarif) {
        this.tarif = tarif;
    }

    public String getTermbanc() {
        return termbanc;
    }

    public void setTermbanc(String termbanc) {
        this.termbanc = termbanc;
    }

    public String getTypea() {
        return typea;
    }

    public void setTypea(String typea) {
        this.typea = typea;
    }

    public String getGeom() {
        return geom;
    }

    public void setGeom(String geom) {
        this.geom = geom;
    }

    public String getGeom_err() {
        return geom_err;
    }

    public void setGeom_err(String geom_err) {
        this.geom_err = geom_err;
    }

    public Long getGeom_o() {
        return geom_o;
    }

    public void setGeom_o(Long geom_o) {
        this.geom_o = geom_o;
    }

    public String getCdate() {
        return cdate;
    }

    public void setCdate(String cdate) {
        this.cdate = cdate;
    }

    public String getMdate() {
        return mdate;
    }

    public void setMdate(String mdate) {
        this.mdate = mdate;
    }
}
