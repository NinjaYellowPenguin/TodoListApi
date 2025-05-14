package yellowpenguin.ninja.core.application.dto.task;

import java.util.List;

public class PaginatedTaskResponse {
	
	private List<TaskResponse> data;
	private int page;
	private int limit;
	private int total;
	
	public List<TaskResponse> getData() {
		return data;
	}
	public void setData(List<TaskResponse> data) {
		this.data = data;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}	

}
