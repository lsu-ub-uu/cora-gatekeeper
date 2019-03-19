module se.uu.ub.cora.gatekeeper {
	requires java.ws.rs;
	requires json;
	requires javax.servlet.api;
	requires bookkeeper;
	requires java.activation;
	// exports se.uu.ub.cora.gatekeeper;

	uses se.uu.ub.cora.gatekeeper.user.UserPickerProvider;
	uses se.uu.ub.cora.gatekeeper.user.UserStorage;
}