package se.uu.ub.cora.gatekeeper.initialize;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.stream.Stream;

public class DummyClassLoader extends ClassLoader {

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		// TODO Auto-generated method stub
		return super.loadClass(name);
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		// TODO Auto-generated method stub
		return super.findClass(name);
	}

	@Override
	protected Class<?> loadClass(String arg0, boolean arg1) throws ClassNotFoundException {
		// TODO Auto-generated method stub
		return super.loadClass(arg0, arg1);
	}

	@Override
	protected Class<?> findClass(String moduleName, String name) {
		// TODO Auto-generated method stub
		return super.findClass(moduleName, name);
	}

	@Override
	protected String findLibrary(String libname) {
		// TODO Auto-generated method stub
		return super.findLibrary(libname);
	}

	@Override
	public void clearAssertionStatus() {
		// TODO Auto-generated method stub
		super.clearAssertionStatus();
	}

	@Override
	protected Package definePackage(String name, String specTitle, String specVersion,
			String specVendor, String implTitle, String implVersion, String implVendor,
			URL sealBase) {
		// TODO Auto-generated method stub
		return super.definePackage(name, specTitle, specVersion, specVendor, implTitle, implVersion,
				implVendor, sealBase);
	}

	@Override
	protected URL findResource(String moduleName, String name) throws IOException {
		// TODO Auto-generated method stub
		return super.findResource(moduleName, name);
	}

	@Override
	protected URL findResource(String name) {
		// TODO Auto-generated method stub
		return super.findResource(name);
	}

	@Override
	protected Enumeration<URL> findResources(String name) throws IOException {
		// TODO Auto-generated method stub
		Enumeration<URL> findResources = super.findResources(name);
		return findResources;
	}

	@Override
	protected Object getClassLoadingLock(String arg0) {
		// TODO Auto-generated method stub
		return super.getClassLoadingLock(arg0);
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		String name = super.getName();
		return "DummyClassLoader";
	}

	@Override
	protected Package getPackage(String name) {
		// TODO Auto-generated method stub
		return super.getPackage(name);
	}

	@Override
	protected Package[] getPackages() {
		// TODO Auto-generated method stub
		return super.getPackages();
	}

	@Override
	public URL getResource(String arg0) {
		// TODO Auto-generated method stub
		return super.getResource(arg0);
	}

	@Override
	public InputStream getResourceAsStream(String arg0) {
		// TODO Auto-generated method stub
		return super.getResourceAsStream(arg0);
	}

	@Override
	public Enumeration<URL> getResources(String name) throws IOException {
		// TODO Auto-generated method stub
		// Enumeration<URL> resources = super.getResources(name);
		// resources.
		// Vector<URL> vector = new Vector<URL>();
		// URL url = new URL(
		//
		// "META-INF/services/se.uu.ub.cora.gatekeeper.user.DummyUserPickerProvider");
		// URL url = new
		// URL("se/uu/ub/cora/gatekeeper/user/DummyUserPickerProvider.class");
		// URL url = new URL("se/uu/ub/cora/gatekeeper/user/");
		// vector.add(url);
		// resources = new Vector(<URL>)
		// return resources;
		// META-INF/services/se.uu.ub.cora.gatekeeper.user.UserPickerProvider
		// Class<?> findClass = findClass("");
		// String fileName =
		// FileTransferClient.class.getResource("input1.txt").getPath();
		// System.out.println(fileName);
		// URL url = file.toURI().toURL();
		// ClassLoader contextClassLoader =
		// Thread.currentThread().getContextClassLoader();
		// // URL resource = contextClassLoader
		// //
		// .getResource("se.uu.ub.cora.gatekeeper.user.DummyUserPickerProvider.class");
		// URL resource = contextClassLoader
		// .getResource("/se.uu.ub.cora.gatekeeper.user.DummyUserPickerProvider.class");
		// URL resource2 = contextClassLoader
		// .getResource("/se/uu/ub/cora/gatekeeper/user/DummyUserPickerProvider.class");
		// URL resource3 =
		// contextClassLoader.getResource("/DummyUserPickerProvider.class");
		// URL resource4 =
		// contextClassLoader.getResource("DummyUserPickerProvider.class");
		//

		// URL resource = getClass().getResource("/DummyUserPickerProvider.class");
		// URL resource2 = getClass().getResource(
		// "META-INF/services/se.uu.ub.cora.gatekeeper.user.DummyUserPickerProvider.class");
		// URL resource3 = getClass()
		// .getResource("se.uu.ub.cora.gatekeeper.user.DummyUserPickerProvider.class");
		//
		// Class.forName("se.uu.ub.cora.gatekeeper.user.DummyUserPickerProvider").
		//
		// Enumeration<URL> enumInstance =
		// Collections.enumeration(Arrays.asList(resource));
		// return enumInstance;

		// Class.forName("se.uu.ub.cora.gatekeeper.UserPickerProviderSpy")
		// ClassLoader urlLoader = ClassLoader.getSystemClassLoader();
		// Enumeration<URL> resources22 =
		// urlLoader.getResources("se.uu.ub.cora.gatekeeper.user");
		// // return resources;
		//
		// // return vector.elements();
		// Enumeration<URL> resources = super.getResources(
		// "META-INF/services/se.uu.ub.cora.gatekeeper.user.DummyUserPickerProvider");
		// return resources;
		return super.getResources(name);
	}

	@Override
	public Stream<URL> resources(String name) {
		// TODO Auto-generated method stub
		return super.resources(name);
	}

	@Override
	public void setClassAssertionStatus(String className, boolean enabled) {
		// TODO Auto-generated method stub
		super.setClassAssertionStatus(className, enabled);
	}

	@Override
	public void setDefaultAssertionStatus(boolean enabled) {
		// TODO Auto-generated method stub
		super.setDefaultAssertionStatus(enabled);
	}

	@Override
	public void setPackageAssertionStatus(String packageName, boolean enabled) {
		// TODO Auto-generated method stub
		super.setPackageAssertionStatus(packageName, enabled);
	}

}
