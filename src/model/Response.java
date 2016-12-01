package model;


public class Response {
	int errno;
	String err;
	Object rsm;
	
	public Response(){
		super();
		this.errno = 3;
		this.err = "";
		this.rsm = new Object();
	}
	public Response(int errno,String err,Object rsm){
		super();
		this.errno = errno;
		this.err = err;
		this.rsm = rsm;
	}
	
	public String getErr(){
		return this.err;
	}
	
	public Object getRsm(){
		return this.rsm;
	}
	public int getErrno() {
		return errno;
	}
	public void setErrno(int errno) {
		this.errno = errno;
	}
	public void setErr(String err) {
		this.err = err;
	}
	public void setRsm(Object rsm) {
		this.rsm = rsm;
	}
}

