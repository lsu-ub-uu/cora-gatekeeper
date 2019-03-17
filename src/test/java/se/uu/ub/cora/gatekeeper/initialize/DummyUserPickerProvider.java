package se.uu.ub.cora.gatekeeper.initialize;

import java.util.Map;

import se.uu.ub.cora.gatekeeper.user.UserPicker;
import se.uu.ub.cora.gatekeeper.user.UserPickerProvider;

public class DummyUserPickerProvider implements UserPickerProvider {
	public DummyUserPickerProvider() {
		// TODO Auto-generated constructor stub
		String s = "";
		s += "lkj";

	}

	@Override
	public UserPicker getUserPicker() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void startUsingInitInfo(Map<String, String> initInfo) {
		// TODO Auto-generated method stub

	}

}
