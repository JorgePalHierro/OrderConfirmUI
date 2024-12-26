package com.example.OrdenConfrimUI;



import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PostLoad;


@Entity
public class Posusuarios {
	 
    private String CNOMUSUA;
    private String CPASSWOR;
    private String CPERFILE;
    private String CNUMTIEN;   
    private String RNOMEMP;
    @Id
    private String RNUMEMP;
    private String RPASSANT1;
    private String RPASSANT2;
    private Double RCADPASS;
    private Character RBLOQACC;
    private Character RVALIDACAD;
    public Posusuarios() {
        // Constructor vacío
    }

    public Posusuarios(String CNOMUSUA, String CPASSWOR, String CPERFILE, String CNUMTIEN, String RNOMEMP, String RNUMEMP, String RPASSANT1, String RPASSANT2, Double RCADPASS, Character RBLOQACC, Character RVALIDACAD) {
        this.CNOMUSUA = CNOMUSUA;
        this.CPASSWOR = CPASSWOR;
        this.CPERFILE = CPERFILE;
        this.CNUMTIEN = CNUMTIEN;
        this.RNOMEMP = RNOMEMP;
        this.RNUMEMP = RNUMEMP;
        this.RPASSANT1 = RPASSANT1;
        this.RPASSANT2 = RPASSANT2;
        this.RCADPASS = RCADPASS;
        this.RBLOQACC = RBLOQACC;
        this.RVALIDACAD = RVALIDACAD;
    }

    public String getCNOMUSUA() {
        return CNOMUSUA;
    }

    public void setCNOMUSUA(String CNOMUSUA) {
        this.CNOMUSUA = CNOMUSUA;
    }

    public String getCPASSWOR() {
        return CPASSWOR;
    }

    public void setCPASSWOR(String CPASSWOR) {
        this.CPASSWOR = CPASSWOR;
    }

    public String getCPERFILE() {
        return CPERFILE;
    }

    public void setCPERFILE(String CPERFILE) {
        this.CPERFILE = CPERFILE;
    }

    public String getCNUMTIEN() {
        return CNUMTIEN;
    }

    public void setCNUMTIEN(String CNUMTIEN) {
        this.CNUMTIEN = CNUMTIEN;
    }

    public String getRNOMEMP() {
        return RNOMEMP;
    }

    public void setRNOMEMP(String RNOMEMP) {
        this.RNOMEMP = RNOMEMP;
    }

    public String getRNUMEMP() {
        return RNUMEMP;
    }

    public void setRNUMEMP(String RNUMEMP) {
        this.RNUMEMP = RNUMEMP;
    }

    public String getRPASSANT1() {
        return RPASSANT1;
    }

    public void setRPASSANT1(String RPASSANT1) {
        this.RPASSANT1 = RPASSANT1;
    }

    public String getRPASSANT2() {
        return RPASSANT2;
    }

    public void setRPASSANT2(String RPASSANT2) {
        this.RPASSANT2 = RPASSANT2;
    }

    public Double getRCADPASS() {
        return RCADPASS;
    }
    public Character getRBLOQACC() {
        return RBLOQACC;
    }

    public void setRBLOQACC(Character RBLOQACC) {
        this.RBLOQACC = RBLOQACC;
    }

    public Character getRVALIDACAD() {
        return RVALIDACAD;
    }

    public void setRVALIDACAD(Character RVALIDACAD) {
        this.RVALIDACAD = RVALIDACAD;
    }
    @PostLoad
    public void adjustIdIfNeeded() {
    	if (this.RNUMEMP.length() < 8) {
            this.RNUMEMP = String.format("%0" + (8 - this.RNUMEMP.length()) + "d%s", 0, this.RNUMEMP);
        }
    	
    }
}
