package ntua.lib.dspace.policy;

import java.util.List;

public interface IPolicy {
	
	public static final String ATTR_ID="id";

	public String getPolicyId();
	
	public List getRules();
	
}
