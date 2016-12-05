/**
 * 实体类
 * @author zhengxr
 *上午10:41:29
 */
public class Area {

	String areaId;
	String areaName;
	String parentId;

	public Area(String areaId, String areaName, String parentId) {
		super();
		this.areaId = areaId;
		this.areaName = areaName;
		this.parentId = parentId;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

}
