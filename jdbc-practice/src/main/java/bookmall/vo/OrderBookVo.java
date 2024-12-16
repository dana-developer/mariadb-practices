package bookmall.vo;

public class OrderBookVo {
	private Long no;
	private Long bookNo;
	private Long orderNo;
	private int quantity;
	private int price;
	private String bookTitle;
	
	public OrderBookVo() {
	}
	
	public OrderBookVo(Long no, Long orderNo, Long bookNo, int quantity, int price, String bookTitle) {
		this.no = no;
		this.orderNo = orderNo;
		this.bookNo = bookNo;
		this.quantity = quantity;
		this.price = price;
		this.bookTitle = bookTitle;
	}
	
	public Long getNo() {
		return no;
	}
	public void setNo(Long no) {
		this.no = no;
	}
	public Long getBookNo() {
		return bookNo;
	}
	public void setBookNo(Long bookNo) {
		this.bookNo = bookNo;
	}
	public Long getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Long orderNo) {
		this.orderNo = orderNo;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getBookTitle() {
		return bookTitle;
	}

	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}

}
