import se.uu.ub.cora.gatekeeper.picker.UserPickerInstanceProvider;
import se.uu.ub.cora.gatekeeper.storage.UserStorageViewInstanceProvider;

module se.uu.ub.cora.gatekeeper {
	requires se.uu.ub.cora.initialize;

	uses UserPickerInstanceProvider;
	uses UserStorageViewInstanceProvider;

	exports se.uu.ub.cora.gatekeeper.picker;
	exports se.uu.ub.cora.gatekeeper.storage;
	exports se.uu.ub.cora.gatekeeper.user;
}