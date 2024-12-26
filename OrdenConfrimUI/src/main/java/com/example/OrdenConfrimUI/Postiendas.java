package com.example.OrdenConfrimUI;



import jakarta.persistence.Entity;
import jakarta.persistence.Id;


@Entity
public class Postiendas {
    @Id
    private String CNUMTIEN;
    private String DDESCRIP;
    private String DDIRECIP;    
	private String DNOMUSUA;
    private String CPASSWOR;
    private String DIRECTOR;
    private String RTDAGROUP;
    private String DIRECTORY_NAME;
    
   

	public String getDIRECTORY_NAME() {
		return DIRECTORY_NAME;
	}

	public void setDIRECTORY_NAME(String dIRECTORY_NAME) {
		DIRECTORY_NAME = dIRECTORY_NAME;
	}

	public Postiendas() {
    	
    }
    
    
	public Postiendas(String cNUMTIEN, String dDESCRIP, String dDIRECIP, String dNOMUSUA, String cPASSWOR,
			String dIRECTOR, String rTDAGROUP, String dIRECTORY_NAME) {
		super();
		CNUMTIEN = cNUMTIEN;
		DDESCRIP = dDESCRIP;
		DDIRECIP = dDIRECIP;
		DNOMUSUA = dNOMUSUA;
		CPASSWOR = cPASSWOR;
		DIRECTOR = dIRECTOR;
		RTDAGROUP = rTDAGROUP;
		DIRECTORY_NAME = dIRECTORY_NAME;
	}

	public String getCNUMTIEN() {
		return CNUMTIEN;
	}
	public void setCNUMTIEN(String cNUMTIEN) {
		CNUMTIEN = cNUMTIEN;
	}
	public String getDDESCRIP() {
		return DDESCRIP;
	}
	public void setDDESCRIP(String dDESCRIP) {
		DDESCRIP = dDESCRIP;
	}
	public String getDDIRECIP() {
		return DDIRECIP;
	}
	public void setDDIRECIP(String dDIRECIP) {
		DDIRECIP = dDIRECIP;
	}
	public String getDNOMUSUA() {
		return DNOMUSUA;
	}
	public void setDNOMUSUA(String dNOMUSUA) {
		DNOMUSUA = dNOMUSUA;
	}
	public String getCPASSWOR() {
		return CPASSWOR;
	}
	public void setCPASSWOR(String cPASSWOR) {
		CPASSWOR = cPASSWOR;
	}
	public String getDIRECTOR() {
		return DIRECTOR;
	}
	public void setDIRECTOR(String dIRECTOR) {
		DIRECTOR = dIRECTOR;
	}
	public String getRTDAGROUP() {
		return RTDAGROUP;
	}
	public void setRTDAGROUP(String rTDAGROUP) {
		RTDAGROUP = rTDAGROUP;
	}
   
   
}