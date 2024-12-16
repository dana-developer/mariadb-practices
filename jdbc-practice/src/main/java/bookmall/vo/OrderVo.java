package bookmall.vo;

public class OrderVo {
	private Long no;
	private String number; //주문번호
	private String status; //
	private int payment;
	private String deliveryAdress;
	private Long userNo;
	
	public OrderVo(Long no, Long userNo, String number, String status, int payment, String shipping) {
		this.no = no;
		this.userNo = userNo;
		this.number = number;
		this.status = status;
		this.payment = payment;
		this.deliveryAdress = shipping;
	}
	
	public OrderVo() {
	}

	public Long getNo() {
		return no;
	}
	public void setNo(Long no) {
		this.no = no;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getPayment() {
		return payment;
	}
	public void setPayment(int payment) {
		this.payment = payment;
	}
	public String getShipping() {
		return deliveryAdress;
	}
	public void setShipping(String deliveryAdress) {
		this.deliveryAdress = deliveryAdress;
	}
	public Long getUserNo() {
		return userNo;
	}
	public void setUserNo(Long userNo) {
		this.userNo = userNo;
	}
	
}
