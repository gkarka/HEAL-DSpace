package ntua.lib.dspace.policy;

import java.util.List;

public class Policy implements IPolicy {

	
	private String policyId;

	private List rules;
	
	public Policy(String policyId, List rules) {
		this.policyId = policyId;
		this.rules = rules;
	}

	public List getRules() {
		return rules;
	}

	public void setRules(List rules) {
		this.rules = rules;
	}

	public String getPolicyId() {
		return policyId;
	}
	

}
