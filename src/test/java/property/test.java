package property;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

public class test {
	@Test
	public void test(){
		List<String> assignerList = new ArrayList<String>();
		assignerList.add("zhangsan");
		assignerList.add("lisi");
		assignerList.add("小名");
		System.out.println(StringUtils.join(assignerList, "/"));
	}
}
