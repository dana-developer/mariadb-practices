package bookshop.vo;

public class AuthorVo {
	// 초반에는 DB의 테이블과 일치하지만, join하는 경우 달라질 수 있음
	private Long id;
	private String name;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "AuthorVo [id=" + id + ", name=" + name + "]";
	}
	
}
