package es.upm.fi.sos.upmbank.cliente;

import es.upm.fi.sos.upmbank.*;
import es.upm.fi.sos.upmbank.cliente.*;
import es.upm.fi.sos.upmbank.cliente.UPMBankWSStub.*;
import es.upm.fi.sos.upmbank.cliente.UPMBankWSCallbackHandler.*;

import java.util.Arrays;
import java.util.Random;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;

public class UPMBankCliente {

	private static int pruebasErroneas = 0;

	private static void comprobarPrueba(boolean a, boolean b) {
		if (a != b) {
			System.out
					.println("Resultado: ERROR	ERROR	ERROR	ERROR	ERROR	ERROR	ERROR	ERROR\n");
			pruebasErroneas++;
		} else {
			System.out.println("Resultado: OK\n");
		}

	}

	public static void main(String[] args) throws Exception {

		String log4jConfPath = "log4j.properties";
		PropertyConfigurator.configure(log4jConfPath);
		// Sin fichero de log
		// BasicConfigurator.configure();
		String nombreUsuario1 = "";
		String contrasenaUsuario1 = "";
		String nombreUsuario2 = "";
		String contrasenaUsuario2 = "";
		String nombreUsuario3 = "";
		String contrasenaUsuario3 = "";
		Login contenedorPeticionServidor;
		LoginResponse respuestaBackEnd;
		AddUserResponseE respuestaServidor;
		String randomString = "";
		Random rand = new Random();
		AddUser user = new AddUser();
		Username username = new Username();
		int contadorPruebas = 1;
		// Creo un cliente
		System.out.println(contadorPruebas + ". Crear cliente\n");
		contadorPruebas++;
		UPMBankWSStub stubServidor1 = new UPMBankWSStub();
		stubServidor1._getServiceClient().getOptions().setManageSession(true);
		stubServidor1._getServiceClient().engageModule("addressing");
		stubServidor1._getServiceClient().getOptions().setTimeOutInMilliSeconds(30000);
		
		// Crear un usuario sin que se haya hecho ningun login
		System.out
				.println(contadorPruebas
						+ ". Crear un usuario sin que se haya hecho ningun login -> error");
		contadorPruebas++;
		rand = new Random();
		randomString = Integer.toString(rand.nextInt(10000));
		nombreUsuario1 = "evaC" + randomString;
		user = new AddUser();
		username = new Username();
		username.setUsername(nombreUsuario1);
		user.setArgs0(username);

		respuestaServidor = new AddUserResponseE();
		respuestaServidor = stubServidor1.addUser(user);

		contrasenaUsuario1 = respuestaServidor.get_return().getPwd();
		System.out.println("Respuesta: "
				+ respuestaServidor.get_return().getResponse() + "		Usuario: "
				+ nombreUsuario1 + "	Pwd: " + contrasenaUsuario1);
		comprobarPrueba(false, respuestaServidor.get_return().getResponse());

		// Hacer login con admin
		System.out.println(contadorPruebas + ". Hacer login con admin -> OK");
		contadorPruebas++;
		contenedorPeticionServidor = new Login();
		respuestaBackEnd = new LoginResponse();

		User userLogin = new User();
		userLogin.setName("admin");
		userLogin.setPwd("admin");
		contenedorPeticionServidor.setArgs0(userLogin);

		respuestaBackEnd = stubServidor1.login(contenedorPeticionServidor);

		System.out.println("Login: "
				+ respuestaBackEnd.get_return().getResponse());
		comprobarPrueba(true, respuestaBackEnd.get_return().getResponse());

		// Crear el mismo usuario pero con un login previo de un admin
		System.out
				.println("4. Crear el mismo usuario pero con un login previo de un admin -> OK");
		respuestaServidor = stubServidor1.addUser(user);

		contrasenaUsuario1 = respuestaServidor.get_return().getPwd();

		System.out.println("Respuesta: "
				+ respuestaServidor.get_return().getResponse() + "		Usuario: "
				+ nombreUsuario1 + "	Pwd: " + contrasenaUsuario1);
		comprobarPrueba(true, respuestaServidor.get_return().getResponse());

		// Crear el mismo usuario una vez que ya está creado
		System.out
				.println("5. Crear el mismo usuario una vez que ya está creado -> error");
		respuestaServidor = stubServidor1.addUser(user);

		System.out.println("Respuesta: "
				+ respuestaServidor.get_return().getResponse() + "		Usuario: "
				+ nombreUsuario1 + "	Pwd: " + contrasenaUsuario1);
		comprobarPrueba(false, respuestaServidor.get_return().getResponse());

		// Hacer login con ese user. Debe rechazarla porque esta la sesión del
		// admin
		System.out
				.println("6. Hacer login con ese user -> error (porque existe sesión de admin)");
		contenedorPeticionServidor = new Login();
		respuestaBackEnd = new LoginResponse();

		userLogin = new User();
		userLogin.setName(nombreUsuario1);
		userLogin.setPwd(respuestaServidor.get_return().getPwd());
		contenedorPeticionServidor.setArgs0(userLogin);

		respuestaBackEnd = stubServidor1.login(contenedorPeticionServidor);
		System.out.println("Login: "
				+ respuestaBackEnd.get_return().getResponse());
		comprobarPrueba(false, respuestaBackEnd.get_return().getResponse());

		// Hacer otro login con admin. Debe funcionar
		System.out.println(contadorPruebas
				+ ". Hacer otro login con admin -> OK");
		contadorPruebas++;
		contenedorPeticionServidor = new Login();
		respuestaBackEnd = new LoginResponse();

		userLogin = new User();
		userLogin.setName("admin");
		userLogin.setPwd("admin");
		contenedorPeticionServidor.setArgs0(userLogin);

		respuestaBackEnd = stubServidor1.login(contenedorPeticionServidor);

		System.out.println("Login: "
				+ respuestaBackEnd.get_return().getResponse());
		comprobarPrueba(true, respuestaBackEnd.get_return().getResponse());

		// Hacer otro login con admin con otra contraseña. Debe funcionar
		System.out
				.println("8. Hacer otro login con admin con otra contraseña -> OK");
		contenedorPeticionServidor = new Login();
		respuestaBackEnd = new LoginResponse();

		userLogin = new User();
		userLogin.setName("admin");
		userLogin.setPwd("culoDeAdmin");
		contenedorPeticionServidor.setArgs0(userLogin);

		respuestaBackEnd = stubServidor1.login(contenedorPeticionServidor);

		System.out.println("Login: "
				+ respuestaBackEnd.get_return().getResponse());
		comprobarPrueba(true, respuestaBackEnd.get_return().getResponse());

		// Crear otro cliente
		System.out.println(contadorPruebas + ". Crear otro cliente");
		contadorPruebas++;
		UPMBankWSStub stubServidor2 = new UPMBankWSStub();
		stubServidor2._getServiceClient().getOptions().setManageSession(true);
		stubServidor2._getServiceClient().engageModule("addressing");
		stubServidor2._getServiceClient().getOptions().setTimeOutInMilliSeconds(30000);

		// Login con el usuario que hemos creado
		System.out.println(contadorPruebas
				+ ". Login con el usuario que hemos creado -> OK");
		contadorPruebas++;
		contenedorPeticionServidor = new Login();
		respuestaBackEnd = new LoginResponse();

		userLogin = new User();
		userLogin.setName(nombreUsuario1);
		userLogin.setPwd(contrasenaUsuario1);
		contenedorPeticionServidor.setArgs0(userLogin);

		respuestaBackEnd = stubServidor2.login(contenedorPeticionServidor);
		System.out.println("Login: "
				+ respuestaBackEnd.get_return().getResponse());
		comprobarPrueba(true, respuestaBackEnd.get_return().getResponse());

		// Crear usuario pero un login previo no admin
		System.out
				.println("11. Crear usuario pero un login previo no admin -> error");
		randomString = Integer.toString(rand.nextInt(10000));
		nombreUsuario2 = "evaC" + randomString;
		username.setUsername(nombreUsuario2);
		user.setArgs0(username);
		respuestaServidor = stubServidor2.addUser(user);

		contrasenaUsuario2 = respuestaServidor.get_return().getPwd();

		System.out.println("Respuesta: "
				+ respuestaServidor.get_return().getResponse() + "		Usuario: "
				+ nombreUsuario2 + "	Pwd: " + contrasenaUsuario2);
		comprobarPrueba(false, respuestaServidor.get_return().getResponse());

		// Otro login con el mismo usuario pero contraseña incorrecta
		System.out
				.println(contadorPruebas
						+ ". Otro login con el mismo usuario pero contraseña incorrecta -> OK (ya existe sesion)");
		contadorPruebas++;
		contenedorPeticionServidor = new Login();
		respuestaBackEnd = new LoginResponse();

		userLogin = new User();
		userLogin.setName(nombreUsuario1);
		userLogin.setPwd("culoUser");
		contenedorPeticionServidor.setArgs0(userLogin);

		respuestaBackEnd = stubServidor2.login(contenedorPeticionServidor);
		System.out.println("Login: "
				+ respuestaBackEnd.get_return().getResponse());
		comprobarPrueba(true, respuestaBackEnd.get_return().getResponse());

		// Crear otro cliente 3
		System.out.println(contadorPruebas + ". Crear otro cliente 3");
		contadorPruebas++;
		UPMBankWSStub stubServidor3 = new UPMBankWSStub();
		stubServidor3._getServiceClient().getOptions().setManageSession(true);
		stubServidor3._getServiceClient().engageModule("addressing");
		stubServidor3._getServiceClient().getOptions().setTimeOutInMilliSeconds(30000);

		// Login con un usuario que no existe
		System.out.println(contadorPruebas
				+ ". Login con un usuario que no existe -> error");
		contadorPruebas++;
		contenedorPeticionServidor = new Login();
		respuestaBackEnd = new LoginResponse();

		userLogin = new User();
		userLogin.setName("gaeruygfcwerhlvgwy3745fgy8v4");
		userLogin.setPwd("cacadelavaca");
		contenedorPeticionServidor.setArgs0(userLogin);

		respuestaBackEnd = stubServidor3.login(contenedorPeticionServidor);
		System.out.println("Login: "
				+ respuestaBackEnd.get_return().getResponse());
		comprobarPrueba(false, respuestaBackEnd.get_return().getResponse());

		// Crear un nuevo usuario en la sesion 1 en la que esta admin
		System.out
				.println(contadorPruebas
						+ ". Crear un nuevo usuario en la sesion 1 en la que esta admin -> OK");
		contadorPruebas++;
		randomString = Integer.toString(rand.nextInt(10000));
		nombreUsuario3 = "evaC" + randomString;
		username.setUsername(nombreUsuario3);
		user.setArgs0(username);
		respuestaServidor = stubServidor1.addUser(user);

		contrasenaUsuario3 = respuestaServidor.get_return().getPwd();

		System.out.println("Respuesta: "
				+ respuestaServidor.get_return().getResponse() + "		Usuario: "
				+ nombreUsuario3 + "	Pwd: " + contrasenaUsuario3);
		comprobarPrueba(true, respuestaServidor.get_return().getResponse());

		// Login con un usuario valido en cliente que ya tiene otro usuario en
		// sesion 2
		System.out
				.println(contadorPruebas
						+ ". Login con un usuario valido en cliente que ya tiene otro usuario en sesion 2 -> error");
		contadorPruebas++;
		contenedorPeticionServidor = new Login();
		respuestaBackEnd = new LoginResponse();

		userLogin = new User();
		userLogin.setName(nombreUsuario3);
		userLogin.setPwd(contrasenaUsuario3);
		contenedorPeticionServidor.setArgs0(userLogin);

		respuestaBackEnd = stubServidor2.login(contenedorPeticionServidor);
		System.out.println("Login: "
				+ respuestaBackEnd.get_return().getResponse());
		comprobarPrueba(false, respuestaBackEnd.get_return().getResponse());

		// Login con un usuario valido en cliente que no tiene ningun usuario en
		// sesion
		System.out
				.println(contadorPruebas
						+ ". Login con un usuario valido en cliente que no tiene ningun usuario en sesion -> OK");
		contadorPruebas++;
		contenedorPeticionServidor = new Login();
		respuestaBackEnd = new LoginResponse();

		userLogin = new User();
		userLogin.setName(nombreUsuario3);
		userLogin.setPwd(contrasenaUsuario3);
		contenedorPeticionServidor.setArgs0(userLogin);

		respuestaBackEnd = stubServidor3.login(contenedorPeticionServidor);
		System.out.println("Login: "
				+ respuestaBackEnd.get_return().getResponse());
		comprobarPrueba(true, respuestaBackEnd.get_return().getResponse());

		// Tercer login de admin en cliente 1
		System.out.println(contadorPruebas
				+ ". Hacer un cuarto login con admin en cliente 1-> OK");
		contadorPruebas++;
		contenedorPeticionServidor = new Login();
		respuestaBackEnd = new LoginResponse();

		userLogin = new User();
		userLogin.setName("admin");
		userLogin.setPwd("admin");
		contenedorPeticionServidor.setArgs0(userLogin);

		respuestaBackEnd = stubServidor1.login(contenedorPeticionServidor);

		System.out.println("Login: "
				+ respuestaBackEnd.get_return().getResponse());
		comprobarPrueba(true, respuestaBackEnd.get_return().getResponse());

		// Crear cliente 4
		System.out.println(contadorPruebas + ". Crear otro cliente 4");
		contadorPruebas++;
		UPMBankWSStub stubServidor4 = new UPMBankWSStub();
		stubServidor4._getServiceClient().getOptions().setManageSession(true);
		stubServidor4._getServiceClient().engageModule("addressing");
		stubServidor4._getServiceClient().getOptions().setTimeOutInMilliSeconds(30000);

		// Cambiar contraseña de usuario sin haber hecho login
		System.out
				.println(contadorPruebas
						+ ". Cambiar contraseña de usuario sin haber hecho login -> error");
		contadorPruebas++;
		ChangePassword user3 = new ChangePassword();
		ChangePasswordResponse respuestaServidor3 = new ChangePasswordResponse();

		PasswordPair pwdPair = new PasswordPair();
		pwdPair.setOldpwd(contrasenaUsuario3);
		pwdPair.setNewpwd("carapan09");
		user3.setArgs0(pwdPair);

		respuestaServidor3 = stubServidor4.changePassword(user3);

		System.out.println("Respuesta: "
				+ respuestaServidor3.get_return().getResponse());
		comprobarPrueba(false, respuestaServidor3.get_return().getResponse());

		// Cambiar contraseña de usuario con sesión pero con contraseña
		// incorrecta
		System.out
				.println(contadorPruebas
						+ ". Cambiar contraseña de usuario con sesión pero con contraseña incorrecta -> error");
		contadorPruebas++;
		user3 = new ChangePassword();
		respuestaServidor3 = new ChangePasswordResponse();

		pwdPair = new PasswordPair();
		pwdPair.setOldpwd("cdurcnkseuyv");
		pwdPair.setNewpwd("carapan09");
		user3.setArgs0(pwdPair);

		respuestaServidor3 = stubServidor3.changePassword(user3);

		System.out.println("Respuesta: "
				+ respuestaServidor3.get_return().getResponse());
		comprobarPrueba(false, respuestaServidor3.get_return().getResponse());

		// Cambiar contraseña de admin con contraseña correcta
		System.out
				.println(contadorPruebas
						+ ". Cambiar contraseña de admin con contraseña correcta -> OK");
		contadorPruebas++;
		user3 = new ChangePassword();
		respuestaServidor3 = new ChangePasswordResponse();

		pwdPair = new PasswordPair();
		pwdPair.setOldpwd("admin");
		pwdPair.setNewpwd("admin1");
		user3.setArgs0(pwdPair);

		respuestaServidor3 = stubServidor1.changePassword(user3);

		System.out.println("Respuesta: "
				+ respuestaServidor3.get_return().getResponse());
		comprobarPrueba(true, respuestaServidor3.get_return().getResponse());

		// Cambiar contraseña de admin con contraseña incorrecta
		System.out
				.println(contadorPruebas
						+ ". Cambiar contraseña de admin con contraseña incorrecta -> error");
		contadorPruebas++;
		user3 = new ChangePassword();
		respuestaServidor3 = new ChangePasswordResponse();

		pwdPair = new PasswordPair();
		pwdPair.setOldpwd("admin");
		pwdPair.setNewpwd("admin1");
		user3.setArgs0(pwdPair);

		respuestaServidor3 = stubServidor1.changePassword(user3);

		System.out.println("Respuesta: "
				+ respuestaServidor3.get_return().getResponse());
		comprobarPrueba(false, respuestaServidor3.get_return().getResponse());

		// Volver a dejar como estaba la contraseña de admin
		System.out.println(contadorPruebas
				+ ". Volver a dejar como estaba la contraseña de admin -> OK");
		contadorPruebas++;
		user3 = new ChangePassword();
		respuestaServidor3 = new ChangePasswordResponse();

		pwdPair = new PasswordPair();
		pwdPair.setOldpwd("admin1");
		pwdPair.setNewpwd("admin");
		user3.setArgs0(pwdPair);

		respuestaServidor3 = stubServidor1.changePassword(user3);

		System.out.println("Respuesta: "
				+ respuestaServidor3.get_return().getResponse());
		comprobarPrueba(true, respuestaServidor3.get_return().getResponse());

		// Cambiar contraseña de usuario existente con contraseña correcta
		System.out
				.println(contadorPruebas
						+ ". Cambiar contraseña de usuario existente con contraseña correcta -> OK");
		contadorPruebas++;
		user3 = new ChangePassword();
		respuestaServidor3 = new ChangePasswordResponse();

		pwdPair = new PasswordPair();
		pwdPair.setOldpwd(contrasenaUsuario3);
		contrasenaUsuario3 = "iefbtwx58ocxj75g";
		pwdPair.setNewpwd(contrasenaUsuario3);
		user3.setArgs0(pwdPair);

		respuestaServidor3 = stubServidor3.changePassword(user3);

		System.out.println("Respuesta: "
				+ respuestaServidor3.get_return().getResponse());
		comprobarPrueba(true, respuestaServidor3.get_return().getResponse());

		// Cambiar contraseña de usuario existente con contraseña correcta por
		// segunda vez
		System.out
				.println(contadorPruebas
						+ ". Cambiar contraseña de usuario existente con contraseña correcta por segunda vez -> OK");
		contadorPruebas++;
		user3 = new ChangePassword();
		respuestaServidor3 = new ChangePasswordResponse();

		pwdPair = new PasswordPair();
		pwdPair.setOldpwd(contrasenaUsuario3);
		contrasenaUsuario3 = "iefbtwx58ocxj75g";
		pwdPair.setNewpwd(contrasenaUsuario3);
		user3.setArgs0(pwdPair);

		respuestaServidor3 = stubServidor3.changePassword(user3);

		System.out.println("Respuesta: "
				+ respuestaServidor3.get_return().getResponse());
		comprobarPrueba(true, respuestaServidor3.get_return().getResponse());

		// Crear 3 nuevos clientes para la prueba de contraseñas
		System.out
				.println(contadorPruebas
						+ ". Crear 4 nuevos clientes para la prueba de contraseñas y movimientos");
		contadorPruebas++;
		UPMBankWSStub stubServidor5 = new UPMBankWSStub();
		stubServidor5._getServiceClient().getOptions().setManageSession(true);
		stubServidor5._getServiceClient().engageModule("addressing");
		stubServidor5._getServiceClient().getOptions().setTimeOutInMilliSeconds(30000);
		UPMBankWSStub stubServidor6 = new UPMBankWSStub();
		stubServidor6._getServiceClient().getOptions().setManageSession(true);
		stubServidor6._getServiceClient().engageModule("addressing");
		stubServidor6._getServiceClient().getOptions().setTimeOutInMilliSeconds(30000);
		UPMBankWSStub stubServidor7 = new UPMBankWSStub();
		stubServidor7._getServiceClient().getOptions().setManageSession(true);
		stubServidor7._getServiceClient().engageModule("addressing");
		stubServidor7._getServiceClient().getOptions().setTimeOutInMilliSeconds(30000);
		UPMBankWSStub stubServidor8 = new UPMBankWSStub();
		stubServidor8._getServiceClient().getOptions().setManageSession(true);
		stubServidor8._getServiceClient().engageModule("addressing");
		stubServidor8._getServiceClient().getOptions().setTimeOutInMilliSeconds(30000);

		// Hacer login en los 3 clientes con el mismo usuario
		System.out
				.println(contadorPruebas
						+ ". Hacer login en los 3 clientes con el mismo usuario -> 3 OK");
		contadorPruebas++;
		contenedorPeticionServidor = new Login();
		respuestaBackEnd = new LoginResponse();
		userLogin = new User();
		userLogin.setName(nombreUsuario1);
		userLogin.setPwd(contrasenaUsuario1);
		contenedorPeticionServidor.setArgs0(userLogin);
		respuestaBackEnd = stubServidor5.login(contenedorPeticionServidor);
		System.out.println("Login: "
				+ respuestaBackEnd.get_return().getResponse());
		comprobarPrueba(true, respuestaBackEnd.get_return().getResponse());
		contenedorPeticionServidor = new Login();
		respuestaBackEnd = new LoginResponse();
		userLogin = new User();
		userLogin.setName(nombreUsuario1);
		userLogin.setPwd(contrasenaUsuario1);
		contenedorPeticionServidor.setArgs0(userLogin);
		respuestaBackEnd = stubServidor6.login(contenedorPeticionServidor);
		System.out.println("Login: "
				+ respuestaBackEnd.get_return().getResponse());
		comprobarPrueba(true, respuestaBackEnd.get_return().getResponse());
		contenedorPeticionServidor = new Login();
		respuestaBackEnd = new LoginResponse();
		userLogin = new User();
		userLogin.setName(nombreUsuario1);
		userLogin.setPwd(contrasenaUsuario1);
		contenedorPeticionServidor.setArgs0(userLogin);
		respuestaBackEnd = stubServidor7.login(contenedorPeticionServidor);
		System.out.println("Login: "
				+ respuestaBackEnd.get_return().getResponse());
		comprobarPrueba(true, respuestaBackEnd.get_return().getResponse());

		// Cambio contraseña en el primer cliente con contraseña con la que hizo
		// login
		System.out
				.println(contadorPruebas
						+ ". Cambio contraseña en el primer cliente con contraseña con la que hizo login -> OK");
		contadorPruebas++;
		user3 = new ChangePassword();
		respuestaServidor3 = new ChangePasswordResponse();

		pwdPair = new PasswordPair();
		pwdPair.setOldpwd(contrasenaUsuario1);
		String contrasenaOriginal1 = contrasenaUsuario1;
		String contrasenaNueva1 = "contrasenaA";
		pwdPair.setNewpwd(contrasenaNueva1);
		user3.setArgs0(pwdPair);

		respuestaServidor3 = stubServidor5.changePassword(user3);

		System.out.println("Respuesta: "
				+ respuestaServidor3.get_return().getResponse());
		comprobarPrueba(true, respuestaServidor3.get_return().getResponse());

		// Cambio contraseña en el segundo cliente con contraseña con la que
		// hizo login
		System.out
				.println(contadorPruebas
						+ ". Cambio contraseña en el segundo cliente con contraseña con la que hizo login -> OK");
		contadorPruebas++;
		user3 = new ChangePassword();
		respuestaServidor3 = new ChangePasswordResponse();

		pwdPair = new PasswordPair();
		pwdPair.setOldpwd(contrasenaOriginal1);
		contrasenaNueva1 = "contrasenaB";
		pwdPair.setNewpwd(contrasenaNueva1);
		user3.setArgs0(pwdPair);

		respuestaServidor3 = stubServidor6.changePassword(user3);

		System.out.println("Respuesta: "
				+ respuestaServidor3.get_return().getResponse());
		comprobarPrueba(true, respuestaServidor3.get_return().getResponse());

		// Cambio contraseña en el tercer cliente con contraseña que ha puesto
		// el segundo cliente
		System.out
				.println(contadorPruebas
						+ ". Cambio contraseña en el tercer cliente con contraseña que ha puesto el segundo cliente -> error");
		contadorPruebas++;
		user3 = new ChangePassword();
		respuestaServidor3 = new ChangePasswordResponse();

		pwdPair = new PasswordPair();
		pwdPair.setOldpwd(contrasenaNueva1);
		pwdPair.setNewpwd("contrasenaC");
		user3.setArgs0(pwdPair);

		respuestaServidor3 = stubServidor7.changePassword(user3);

		System.out.println("Respuesta: "
				+ respuestaServidor3.get_return().getResponse());
		comprobarPrueba(false, respuestaServidor3.get_return().getResponse());

		// Cambio contraseña en el tercer cliente con contraseña con la que
		// hizo login

		System.out
				.println(contadorPruebas
						+ ". Cambio contraseña en el tercer cliente con contraseña con la que hizo login -> OK");
		contadorPruebas++;
		user3 = new ChangePassword();
		respuestaServidor3 = new ChangePasswordResponse();

		pwdPair = new PasswordPair();
		pwdPair.setOldpwd(contrasenaOriginal1);
		contrasenaNueva1 = "contrasenaC";
		pwdPair.setNewpwd(contrasenaNueva1);
		user3.setArgs0(pwdPair);

		respuestaServidor3 = stubServidor7.changePassword(user3);

		System.out.println("Respuesta: "
				+ respuestaServidor3.get_return().getResponse());
		comprobarPrueba(true, respuestaServidor3.get_return().getResponse());

		// Crear cuenta sin hacer login
		System.out.println(contadorPruebas
				+ ". Crear cuenta sin hacer login -> error");
		contadorPruebas++;
		AddBankAcc cuenta1 = new AddBankAcc();
		Deposit quantity = new Deposit();
		quantity.setQuantity(10);
		cuenta1.setArgs0(quantity);
		AddBankAccResponse respuestaServidor4 = new AddBankAccResponse();

		respuestaServidor4 = stubServidor4.addBankAcc(cuenta1);

		System.out.println("Respuesta: "
				+ respuestaServidor4.get_return().getResult() + "	IBAN: "
				+ respuestaServidor4.get_return().getIBAN());
		comprobarPrueba(false, respuestaServidor4.get_return().getResult());

		// Crear cuenta por parte de admin
		System.out.println(contadorPruebas
				+ ". Crear cuenta por parte de admin -> OK");
		contadorPruebas++;
		cuenta1 = new AddBankAcc();
		quantity = new Deposit();
		quantity.setQuantity(10);
		cuenta1.setArgs0(quantity);
		respuestaServidor4 = new AddBankAccResponse();

		respuestaServidor4 = stubServidor1.addBankAcc(cuenta1);
		String iban3 = respuestaServidor4.get_return().getIBAN();
		System.out.println("Respuesta: "
				+ respuestaServidor4.get_return().getResult() + "		IBAN: "
				+ respuestaServidor4.get_return().getIBAN());
		comprobarPrueba(true, respuestaServidor4.get_return().getResult());

		// Crear cuenta de un usuario normal válido con saldo negativo

		System.out
				.println(contadorPruebas
						+ ". Crear cuenta de un usuario normal válido con saldo negativo -> error");
		contadorPruebas++;
		cuenta1 = new AddBankAcc();
		quantity = new Deposit();
		quantity.setQuantity(-10);
		cuenta1.setArgs0(quantity);
		respuestaServidor4 = new AddBankAccResponse();

		respuestaServidor4 = stubServidor2.addBankAcc(cuenta1);

		System.out.println("Respuesta: "
				+ respuestaServidor4.get_return().getResult() + "		IBAN: "
				+ respuestaServidor4.get_return().getIBAN());
		comprobarPrueba(false, respuestaServidor4.get_return().getResult());

		// Crear cuenta de un usuario normal válido con saldo 0

		System.out
				.println(contadorPruebas
						+ ". Crear cuenta de un usuario normal válido con saldo 0 -> OK");
		contadorPruebas++;
		cuenta1 = new AddBankAcc();
		quantity = new Deposit();

		quantity.setQuantity(0);
		cuenta1.setArgs0(quantity);
		respuestaServidor4 = new AddBankAccResponse();

		respuestaServidor4 = stubServidor2.addBankAcc(cuenta1);
		String iban1 = respuestaServidor4.get_return().getIBAN();
		System.out.println("Respuesta: "
				+ respuestaServidor4.get_return().getResult() + "		IBAN: "
				+ respuestaServidor4.get_return().getIBAN());
		comprobarPrueba(true, respuestaServidor4.get_return().getResult());

		// Crear cuenta de un usuario normal válido con saldo positivo

		System.out
				.println(contadorPruebas
						+ ". Crear cuenta de un usuario normal válido con saldo positivo -> OK");
		contadorPruebas++;
		cuenta1 = new AddBankAcc();
		quantity = new Deposit();

		quantity.setQuantity(19.80);

		cuenta1.setArgs0(quantity);
		respuestaServidor4 = new AddBankAccResponse();

		respuestaServidor4 = stubServidor2.addBankAcc(cuenta1);
		String iban2 = respuestaServidor4.get_return().getIBAN();
		System.out.println("Respuesta: "
				+ respuestaServidor4.get_return().getResult() + "		IBAN: "
				+ respuestaServidor4.get_return().getIBAN());
		comprobarPrueba(true, respuestaServidor4.get_return().getResult());

		// Añadir ingreso a cuenta sin estar logado
		System.out.println(contadorPruebas
				+ ". Añadir ingreso a cuenta sin estar logado -> error");
		contadorPruebas++;
		AddIncome income = new AddIncome();
		Movement movement = new Movement();
		movement.setIBAN(iban1);
		movement.setQuantity(10);
		income.setArgs0(movement);

		AddIncomeResponse respuestaServidor6 = new AddIncomeResponse();
		respuestaServidor6 = stubServidor4.addIncome(income);

		System.out.println("Respuesta: "
				+ respuestaServidor6.get_return().getResult() + "		Saldo: "
				+ respuestaServidor6.get_return().getBalance());
		comprobarPrueba(false, respuestaServidor6.get_return().getResult());

		// Añadir ingreso a cuenta que no es del usuario que está logado
		System.out
				.println(contadorPruebas
						+ ". Añadir ingreso a cuenta que no es del usuario que está logado -> error");
		contadorPruebas++;
		income = new AddIncome();
		movement = new Movement();
		movement.setIBAN(iban1);
		movement.setQuantity(10);
		income.setArgs0(movement);

		respuestaServidor6 = new AddIncomeResponse();
		respuestaServidor6 = stubServidor3.addIncome(income);

		System.out.println("Respuesta: "
				+ respuestaServidor6.get_return().getResult() + "		Saldo: "
				+ respuestaServidor6.get_return().getBalance());
		comprobarPrueba(false, respuestaServidor6.get_return().getResult());

		// Añadir ingreso a cuenta con saldo negativo
		System.out.println(contadorPruebas
				+ ". Añadir ingreso a cuenta con saldo negativo -> error");
		contadorPruebas++;
		income = new AddIncome();
		movement = new Movement();
		movement.setIBAN(iban1);
		movement.setQuantity(-10);
		income.setArgs0(movement);

		respuestaServidor6 = new AddIncomeResponse();
		respuestaServidor6 = stubServidor2.addIncome(income);

		System.out.println("Respuesta: "
				+ respuestaServidor6.get_return().getResult() + "		Saldo: "
				+ respuestaServidor6.get_return().getBalance());
		comprobarPrueba(false, respuestaServidor6.get_return().getResult());

		// Añadir ingreso a cuenta con saldo positivo
		System.out.println(contadorPruebas
				+ ". Añadir ingreso a cuenta con saldo positivo -> OK");
		contadorPruebas++;
		income = new AddIncome();
		movement = new Movement();
		movement.setIBAN(iban1);
		movement.setQuantity(100);
		income.setArgs0(movement);

		respuestaServidor6 = new AddIncomeResponse();
		respuestaServidor6 = stubServidor2.addIncome(income);

		System.out.println("Respuesta: "
				+ respuestaServidor6.get_return().getResult() + "		Saldo: "
				+ respuestaServidor6.get_return().getBalance());
		comprobarPrueba(true, respuestaServidor6.get_return().getResult());

		// Añadir ingreso a cuenta que no existe
		System.out.println(contadorPruebas
				+ ". Añadir ingreso a cuenta que no existe -> error");
		contadorPruebas++;
		income = new AddIncome();
		movement = new Movement();
		movement.setIBAN("tortuga");
		movement.setQuantity(10);
		income.setArgs0(movement);

		respuestaServidor6 = new AddIncomeResponse();
		respuestaServidor6 = stubServidor2.addIncome(income);

		System.out.println("Respuesta: "
				+ respuestaServidor6.get_return().getResult() + "		Saldo: "
				+ respuestaServidor6.get_return().getBalance());
		comprobarPrueba(false, respuestaServidor6.get_return().getResult());

		// 3 ingresos en cuenta para tener movimientos
		System.out.println(contadorPruebas
				+ ". 3 ingresos en cuenta para tener movimientos -> OK");
		contadorPruebas++;
		income = new AddIncome();
		movement = new Movement();
		movement.setIBAN(iban1);
		movement.setQuantity(30.20);
		income.setArgs0(movement);

		respuestaServidor6 = new AddIncomeResponse();
		respuestaServidor6 = stubServidor2.addIncome(income);

		System.out.println("Respuesta: "
				+ respuestaServidor6.get_return().getResult() + "		Saldo: "
				+ respuestaServidor6.get_return().getBalance());
		comprobarPrueba(true, respuestaServidor6.get_return().getResult());

		income = new AddIncome();
		movement = new Movement();
		movement.setIBAN(iban1);
		movement.setQuantity(12.80);
		income.setArgs0(movement);

		respuestaServidor6 = new AddIncomeResponse();
		respuestaServidor6 = stubServidor2.addIncome(income);

		System.out.println("Respuesta: "
				+ respuestaServidor6.get_return().getResult() + "		Saldo: "
				+ respuestaServidor6.get_return().getBalance());
		comprobarPrueba(true, respuestaServidor6.get_return().getResult());

		income = new AddIncome();
		movement = new Movement();
		movement.setIBAN(iban2);
		movement.setQuantity(300);
		income.setArgs0(movement);

		respuestaServidor6 = new AddIncomeResponse();
		respuestaServidor6 = stubServidor2.addIncome(income);

		System.out.println("Respuesta: "
				+ respuestaServidor6.get_return().getResult() + "		Saldo: "
				+ respuestaServidor6.get_return().getBalance());
		comprobarPrueba(true, respuestaServidor6.get_return().getResult());

		// Crear cuenta de un usuario normal válido con saldo positivo

		System.out
				.println(contadorPruebas
						+ ". Crear cuenta de un usuario normal válido con saldo 0 -> OK");
		contadorPruebas++;
		cuenta1 = new AddBankAcc();
		quantity = new Deposit();

		quantity.setQuantity(0);

		cuenta1.setArgs0(quantity);
		respuestaServidor4 = new AddBankAccResponse();

		respuestaServidor4 = stubServidor2.addBankAcc(cuenta1);
		String iban4 = respuestaServidor4.get_return().getIBAN();
		System.out.println("Respuesta: "
				+ respuestaServidor4.get_return().getResult() + "		IBAN: "
				+ respuestaServidor4.get_return().getIBAN());
		comprobarPrueba(true, respuestaServidor4.get_return().getResult());

		// Hacer retirada a cuenta sin estar logado
		System.out.println(contadorPruebas
				+ ". Hacer retirada a cuenta sin estar logado -> error");
		contadorPruebas++;
		AddWithdrawal withdrawal = new AddWithdrawal();
		movement = new Movement();
		movement.setIBAN(iban1);
		movement.setQuantity(10);
		withdrawal.setArgs0(movement);

		AddWithdrawalResponse respuestaServidor7 = new AddWithdrawalResponse();
		respuestaServidor7 = stubServidor4.addWithdrawal(withdrawal);

		System.out.println("Respuesta: "
				+ respuestaServidor7.get_return().getResult() + "		Saldo: "
				+ respuestaServidor7.get_return().getBalance());
		comprobarPrueba(false, respuestaServidor7.get_return().getResult());

		// Hacer retirada a cuenta que no es del usuario que está logado
		System.out
				.println(contadorPruebas
						+ ". Hacer retirada a cuenta que no es del usuario que está logado -> error");
		contadorPruebas++;
		withdrawal = new AddWithdrawal();
		movement = new Movement();
		movement.setIBAN(iban1);
		movement.setQuantity(10);
		withdrawal.setArgs0(movement);

		respuestaServidor7 = new AddWithdrawalResponse();
		respuestaServidor7 = stubServidor3.addWithdrawal(withdrawal);

		System.out.println("Respuesta: "
				+ respuestaServidor7.get_return().getResult() + "		Saldo: "
				+ respuestaServidor7.get_return().getBalance());
		comprobarPrueba(false, respuestaServidor7.get_return().getResult());

		// Hacer retirada a cuenta con saldo negativo
		System.out.println(contadorPruebas
				+ ". Hacer retirada a cuenta con saldo negativo -> error");
		contadorPruebas++;
		withdrawal = new AddWithdrawal();
		movement = new Movement();
		movement.setIBAN(iban1);
		movement.setQuantity(-10);
		withdrawal.setArgs0(movement);

		respuestaServidor7 = new AddWithdrawalResponse();
		respuestaServidor7 = stubServidor2.addWithdrawal(withdrawal);

		System.out.println("Respuesta: "
				+ respuestaServidor7.get_return().getResult() + "		Saldo: "
				+ respuestaServidor7.get_return().getBalance());
		comprobarPrueba(false, respuestaServidor7.get_return().getResult());

		// Hacer retirada a cuenta con saldo positivo
		System.out.println(contadorPruebas
				+ ". Hacer retirada a cuenta con saldo positivo -> OK");
		contadorPruebas++;
		withdrawal = new AddWithdrawal();
		movement = new Movement();
		movement.setIBAN(iban1);
		movement.setQuantity(10);
		withdrawal.setArgs0(movement);

		respuestaServidor7 = new AddWithdrawalResponse();
		respuestaServidor7 = stubServidor2.addWithdrawal(withdrawal);

		System.out.println("Respuesta: "
				+ respuestaServidor7.get_return().getResult() + "		Saldo: "
				+ respuestaServidor7.get_return().getBalance());
		comprobarPrueba(true, respuestaServidor7.get_return().getResult());

		// Hacer retirada a cuenta con saldo positivo
		System.out.println(contadorPruebas
				+ ". Hacer retirada a cuenta con saldo positivo -> OK");
		contadorPruebas++;
		withdrawal = new AddWithdrawal();
		movement = new Movement();
		movement.setIBAN(iban1);
		movement.setQuantity(20);
		withdrawal.setArgs0(movement);

		respuestaServidor7 = new AddWithdrawalResponse();
		respuestaServidor7 = stubServidor2.addWithdrawal(withdrawal);

		System.out.println("Respuesta: "
				+ respuestaServidor7.get_return().getResult() + "		Saldo: "
				+ respuestaServidor7.get_return().getBalance());
		comprobarPrueba(true, respuestaServidor7.get_return().getResult());

		// Hacer retirada a cuenta con saldo positivo
		System.out.println(contadorPruebas
				+ ". Hacer retirada a cuenta con saldo positivo -> OK");
		contadorPruebas++;
		withdrawal = new AddWithdrawal();
		movement = new Movement();
		movement.setIBAN(iban2);
		movement.setQuantity(15);
		withdrawal.setArgs0(movement);

		respuestaServidor7 = new AddWithdrawalResponse();
		respuestaServidor7 = stubServidor2.addWithdrawal(withdrawal);

		System.out.println("Respuesta: "
				+ respuestaServidor7.get_return().getResult() + "		Saldo: "
				+ respuestaServidor7.get_return().getBalance());
		comprobarPrueba(true, respuestaServidor7.get_return().getResult());

		// Hacer retirada a cuenta con saldo positivo
		System.out.println(contadorPruebas
				+ ". Hacer retirada a cuenta con saldo positivo -> OK");
		contadorPruebas++;
		withdrawal = new AddWithdrawal();
		movement = new Movement();
		movement.setIBAN(iban1);
		movement.setQuantity(5);
		withdrawal.setArgs0(movement);

		respuestaServidor7 = new AddWithdrawalResponse();
		respuestaServidor7 = stubServidor2.addWithdrawal(withdrawal);

		System.out.println("Respuesta: "
				+ respuestaServidor7.get_return().getResult() + "		Saldo: "
				+ respuestaServidor7.get_return().getBalance());
		comprobarPrueba(true, respuestaServidor7.get_return().getResult());

		// Hacer retirada a cuenta con saldo positivo
		System.out.println(contadorPruebas
				+ ". Hacer retirada a cuenta con saldo positivo -> OK");
		contadorPruebas++;
		withdrawal = new AddWithdrawal();
		movement = new Movement();
		movement.setIBAN(iban1);
		movement.setQuantity(2);
		withdrawal.setArgs0(movement);

		respuestaServidor7 = new AddWithdrawalResponse();
		respuestaServidor7 = stubServidor2.addWithdrawal(withdrawal);

		System.out.println("Respuesta: "
				+ respuestaServidor7.get_return().getResult() + "		Saldo: "
				+ respuestaServidor7.get_return().getBalance());
		comprobarPrueba(true, respuestaServidor7.get_return().getResult());

		// Hacer un ingreso con cantidad positivo
		System.out.println(contadorPruebas
				+ ". un ingreso con cantidad positivo -> OK");
		contadorPruebas++;
		income = new AddIncome();
		movement = new Movement();
		movement.setIBAN(iban2);
		movement.setQuantity(50);
		income.setArgs0(movement);

		respuestaServidor6 = new AddIncomeResponse();
		respuestaServidor6 = stubServidor2.addIncome(income);

		System.out.println("Respuesta: "
				+ respuestaServidor6.get_return().getResult() + "		Saldo: "
				+ respuestaServidor6.get_return().getBalance());
		comprobarPrueba(true, respuestaServidor6.get_return().getResult());

		// Hacer un ingreso con cantidad positivo
		System.out.println(contadorPruebas
				+ ". un ingreso con cantidad positivo -> OK");
		contadorPruebas++;
		income = new AddIncome();
		movement = new Movement();
		movement.setIBAN(iban1);
		movement.setQuantity(60);
		income.setArgs0(movement);

		respuestaServidor6 = new AddIncomeResponse();
		respuestaServidor6 = stubServidor2.addIncome(income);

		System.out.println("Respuesta: "
				+ respuestaServidor6.get_return().getResult() + "		Saldo: "
				+ respuestaServidor6.get_return().getBalance());
		comprobarPrueba(true, respuestaServidor6.get_return().getResult());

		// Hacer retirada a cuenta que no existe
		System.out.println(contadorPruebas
				+ ". Hacer retirada a cuenta que no existe -> error");
		contadorPruebas++;
		withdrawal = new AddWithdrawal();
		movement = new Movement();
		movement.setIBAN("mortadelo");
		movement.setQuantity(10);
		withdrawal.setArgs0(movement);

		respuestaServidor7 = new AddWithdrawalResponse();
		respuestaServidor7 = stubServidor2.addWithdrawal(withdrawal);

		System.out.println("Respuesta: "
				+ respuestaServidor7.get_return().getResult() + "		Saldo: "
				+ respuestaServidor7.get_return().getBalance());
		comprobarPrueba(false, respuestaServidor7.get_return().getResult());

		// Hacer retirada a cuenta con saldo positivo a una cuenta de admin
		System.out
				.println(contadorPruebas
						+ ". Hacer retirada a cuenta con saldo positivo a una cuenta de admin -> OK");
		contadorPruebas++;
		withdrawal = new AddWithdrawal();
		movement = new Movement();
		movement.setIBAN(iban3);
		movement.setQuantity(7);
		withdrawal.setArgs0(movement);

		respuestaServidor7 = new AddWithdrawalResponse();
		respuestaServidor7 = stubServidor1.addWithdrawal(withdrawal);

		System.out.println("Respuesta: "
				+ respuestaServidor7.get_return().getResult() + "		Saldo: "
				+ respuestaServidor7.get_return().getBalance());
		comprobarPrueba(true, respuestaServidor7.get_return().getResult());

		// Hacer un ingreso con cantidad positivo a una cuenta de admin
		System.out
				.println(contadorPruebas
						+ ". un ingreso con cantidad positivo a una cuenta de admin -> OK");
		contadorPruebas++;
		income = new AddIncome();
		movement = new Movement();
		movement.setIBAN(iban3);
		movement.setQuantity(4);
		income.setArgs0(movement);

		respuestaServidor6 = new AddIncomeResponse();
		respuestaServidor6 = stubServidor1.addIncome(income);

		System.out.println("Respuesta: "
				+ respuestaServidor6.get_return().getResult() + "		Saldo: "
				+ respuestaServidor6.get_return().getBalance());
		comprobarPrueba(true, respuestaServidor6.get_return().getResult());

		// Obtener movimientos sin haber hecho login
		System.out.println(contadorPruebas
				+ ". Obtener movimientos sin haber hecho login -> error");
		contadorPruebas++;
		GetMyMovements ultimosMovimientos = new GetMyMovements();
		GetMyMovementsResponse respuestaServidor8 = new GetMyMovementsResponse();
		respuestaServidor8 = stubServidor4.getMyMovements(ultimosMovimientos);

		System.out.println("Respuesta: "
				+ respuestaServidor8.get_return().getResult()
				+ "		Movimientos: "
				+ Arrays.toString(respuestaServidor8.get_return()
						.getMovementQuantities()));
		comprobarPrueba(false, respuestaServidor8.get_return().getResult());

		// Obtener movimientos de un cliente que no tiene ninguna operación en
		// sus cuentas
		System.out
				.println(contadorPruebas
						+ ". Obtener movimientos de un cliente que no tiene ninguna operación en sus cuentas -> error");
		contadorPruebas++;
		ultimosMovimientos = new GetMyMovements();
		respuestaServidor8 = new GetMyMovementsResponse();
		respuestaServidor8 = stubServidor4.getMyMovements(ultimosMovimientos);

		System.out.println("Respuesta: "
				+ respuestaServidor8.get_return().getResult()
				+ "		Movimientos: "
				+ Arrays.toString(respuestaServidor8.get_return()
						.getMovementQuantities()));
		comprobarPrueba(false, respuestaServidor8.get_return().getResult());

		// Obtener movimientos de un cliente con menos de 10 movimientos
		System.out
				.println(contadorPruebas
						+ ". Obtener movimientos de un cliente con menos de 10 movimientos -> OK");
		contadorPruebas++;
		ultimosMovimientos = new GetMyMovements();
		respuestaServidor8 = new GetMyMovementsResponse();
		respuestaServidor8 = stubServidor1.getMyMovements(ultimosMovimientos);

		System.out.println("Respuesta: "
				+ respuestaServidor8.get_return().getResult()
				+ "		Movimientos: "
				+ Arrays.toString(respuestaServidor8.get_return()
						.getMovementQuantities()));
		comprobarPrueba(true, respuestaServidor8.get_return().getResult());

		// Creo un cliente
		System.out.println(contadorPruebas + ". Crear cliente\n");
		contadorPruebas++;
		UPMBankWSStub stubServidor9 = new UPMBankWSStub();
		stubServidor9._getServiceClient().getOptions().setManageSession(true);
		stubServidor9._getServiceClient().engageModule("addressing");
		stubServidor9._getServiceClient().getOptions().setTimeOutInMilliSeconds(30000);

		// Login de usuario que movimientos en otro cliente distinto para ver su
		// persistencia despues de haber hecho logut en todas las sesiones
		System.out
				.println(contadorPruebas
						+ ". Login de usuario que movimientos en otro cliente distinto para ver su persistencia despues de haber hecho logut en todas las sesiones -> OK");
		contadorPruebas++;
		contenedorPeticionServidor = new Login();
		respuestaBackEnd = new LoginResponse();

		userLogin = new User();
		userLogin.setName("admin");
		userLogin.setPwd("admin");
		contenedorPeticionServidor.setArgs0(userLogin);

		respuestaBackEnd = stubServidor9.login(contenedorPeticionServidor);
		System.out.println("Login: "
				+ respuestaBackEnd.get_return().getResponse());
		comprobarPrueba(true, respuestaBackEnd.get_return().getResponse());

		// Obtener movimientos de un cliente con menos de 10 movimientos
		System.out
				.println(contadorPruebas
						+ ". Obtener movimientos de un cliente con menos de 10 movimientos -> OK");
		contadorPruebas++;
		ultimosMovimientos = new GetMyMovements();
		respuestaServidor8 = new GetMyMovementsResponse();
		respuestaServidor8 = stubServidor9.getMyMovements(ultimosMovimientos);

		System.out.println("Respuesta: "
				+ respuestaServidor8.get_return().getResult()
				+ "		Movimientos: "
				+ Arrays.toString(respuestaServidor8.get_return()
						.getMovementQuantities()));
		comprobarPrueba(true, respuestaServidor8.get_return().getResult());

		// 1 Logout en sesion 2
		System.out.println(contadorPruebas + ". Logout sesion 3\n");
		contadorPruebas++;
		Logout logout2 = new Logout();
		stubServidor9.logout(logout2);

		// Obtener movimientos de un cliente con más de 10 movimientos
		System.out
				.println(contadorPruebas
						+ ". Obtener movimientos de un cliente con más de 10 movimientos -> OK");
		contadorPruebas++;
		ultimosMovimientos = new GetMyMovements();
		respuestaServidor8 = new GetMyMovementsResponse();
		respuestaServidor8 = stubServidor2.getMyMovements(ultimosMovimientos);

		System.out.println("Respuesta: "
				+ respuestaServidor8.get_return().getResult()
				+ "		Movimientos: "
				+ Arrays.toString(respuestaServidor8.get_return()
						.getMovementQuantities()));
		comprobarPrueba(true, respuestaServidor8.get_return().getResult());

		// Obtener movimientos de un cliente que no tiene cuentas
		System.out
				.println(contadorPruebas
						+ ". Obtener movimientos de un cliente que no tiene cuentas -> OK");
		contadorPruebas++;
		ultimosMovimientos = new GetMyMovements();
		respuestaServidor8 = new GetMyMovementsResponse();
		respuestaServidor8 = stubServidor3.getMyMovements(ultimosMovimientos);

		System.out.println("Respuesta: "
				+ respuestaServidor8.get_return().getResult()
				+ "		Movimientos: "
				+ Arrays.toString(respuestaServidor8.get_return()
						.getMovementQuantities()));
		comprobarPrueba(true, respuestaServidor8.get_return().getResult());

		// Crear un cliente nuevo
		System.out.println(contadorPruebas + ". Crear un cliente nuevo -> OK");
		contadorPruebas++;
		randomString = Integer.toString(rand.nextInt(10000));
		String nombreUsuario6 = "evaC" + randomString;
		String contrasenaUsuario6;
		user = new AddUser();
		username = new Username();
		username.setUsername(nombreUsuario6);
		user.setArgs0(username);

		respuestaServidor = new AddUserResponseE();
		respuestaServidor = stubServidor1.addUser(user);

		contrasenaUsuario6 = respuestaServidor.get_return().getPwd();
		System.out.println("Respuesta: "
				+ respuestaServidor.get_return().getResponse() + "		Usuario: "
				+ nombreUsuario6 + "	Pwd: " + contrasenaUsuario6);
		comprobarPrueba(true, respuestaServidor.get_return().getResponse());

		// Login de cliente nuevo en stub vacio
		System.out.println(contadorPruebas
				+ ". Login de cliente nuevo en stub vacio -> OK");
		contadorPruebas++;
		contenedorPeticionServidor = new Login();
		respuestaBackEnd = new LoginResponse();

		userLogin = new User();
		userLogin.setName(nombreUsuario6);
		userLogin.setPwd(contrasenaUsuario6);
		contenedorPeticionServidor.setArgs0(userLogin);

		respuestaBackEnd = stubServidor8.login(contenedorPeticionServidor);
		System.out.println("Login: "
				+ respuestaBackEnd.get_return().getResponse());
		comprobarPrueba(true, respuestaBackEnd.get_return().getResponse());

		// Crear una cuenta para ese cliente
		System.out
				.println(contadorPruebas
						+ ". Crear cuenta de un usuario normal válido con saldo 0 -> OK");
		contadorPruebas++;
		cuenta1 = new AddBankAcc();
		quantity = new Deposit();

		quantity.setQuantity(0);
		cuenta1.setArgs0(quantity);
		AddBankAccResponse respuestaServidor9 = new AddBankAccResponse();

		respuestaServidor9 = stubServidor8.addBankAcc(cuenta1);
		String iban5 = respuestaServidor9.get_return().getIBAN();
		System.out.println("Respuesta: "
				+ respuestaServidor9.get_return().getResult() + "		IBAN: "
				+ respuestaServidor9.get_return().getIBAN());
		comprobarPrueba(true, respuestaServidor9.get_return().getResult());

		// Obtener movimientos de un cliente que tiene cuentas sin movimientos
		System.out
				.println(contadorPruebas
						+ ". Obtener movimientos de un cliente que tiene cuentas sin movimientos -> OK");
		contadorPruebas++;
		ultimosMovimientos = new GetMyMovements();
		respuestaServidor8 = new GetMyMovementsResponse();
		respuestaServidor8 = stubServidor8.getMyMovements(ultimosMovimientos);

		System.out.println("Respuesta: "
				+ respuestaServidor8.get_return().getResult()
				+ "		Movimientos: "
				+ Arrays.toString(respuestaServidor8.get_return()
						.getMovementQuantities()));
		comprobarPrueba(true, respuestaServidor8.get_return().getResult());

		// Borrar una cuenta cuyo saldo no es 0
		System.out.println(contadorPruebas
				+ ". Borrar una cuenta cuyo saldo no es 0 -> error");
		contadorPruebas++;
		CloseBankAccResponse respuestaServidor5 = new CloseBankAccResponse();
		CloseBankAcc closeBankAcc = new CloseBankAcc();
		BankAccount bank = new BankAccount();
		bank.setIBAN(iban2);
		closeBankAcc.setArgs0(bank);

		respuestaServidor5 = stubServidor2.closeBankAcc(closeBankAcc);

		System.out.println("Respuesta: "
				+ respuestaServidor5.get_return().getResponse());
		comprobarPrueba(false, respuestaServidor5.get_return().getResponse());

		// Borrar una cuenta que no pertenece al usuario logado
		System.out
				.println(contadorPruebas
						+ ". Borrar una cuenta que no pertenece al usuario logado -> error");
		contadorPruebas++;
		respuestaServidor5 = new CloseBankAccResponse();
		closeBankAcc = new CloseBankAcc();
		bank = new BankAccount();
		bank.setIBAN(iban3);
		closeBankAcc.setArgs0(bank);

		respuestaServidor5 = stubServidor2.closeBankAcc(closeBankAcc);

		System.out.println("Respuesta: "
				+ respuestaServidor5.get_return().getResponse());
		comprobarPrueba(false, respuestaServidor5.get_return().getResponse());

		// Borrar una cuenta cuando no hay sesión
		System.out.println(contadorPruebas
				+ ". Borrar una cuenta cuando no hay sesión -> error");
		contadorPruebas++;
		respuestaServidor5 = new CloseBankAccResponse();
		closeBankAcc = new CloseBankAcc();
		bank = new BankAccount();
		bank.setIBAN(iban1);
		closeBankAcc.setArgs0(bank);

		respuestaServidor5 = stubServidor4.closeBankAcc(closeBankAcc);

		System.out.println("Respuesta: "
				+ respuestaServidor5.get_return().getResponse());
		comprobarPrueba(false, respuestaServidor5.get_return().getResponse());

		// Borrar una cuenta que se corresponde al usuario logado y cuyo saldo
		// es 0
		System.out
				.println(contadorPruebas
						+ ". Borrar una cuenta que se corresponde al usuario logado y cuyo saldo es 0 -> OK");
		contadorPruebas++;
		respuestaServidor5 = new CloseBankAccResponse();
		closeBankAcc = new CloseBankAcc();
		bank = new BankAccount();
		bank.setIBAN(iban4);
		closeBankAcc.setArgs0(bank);

		respuestaServidor5 = stubServidor2.closeBankAcc(closeBankAcc);

		System.out.println("Respuesta: "
				+ respuestaServidor5.get_return().getResponse());
		comprobarPrueba(true, respuestaServidor5.get_return().getResponse());

		// Usuario normal intenta borrar usuario existente
		System.out.println(contadorPruebas
				+ ". Usuario normal intenta borrar usuario existente -> error");
		contadorPruebas++;
		RemoveUser user2 = new RemoveUser();
		RemoveUserResponse respuestaServidor2 = new RemoveUserResponse();

		username.setUsername(nombreUsuario3);
		user2.setArgs0(username);

		respuestaServidor2 = stubServidor2.removeUser(user2);

		System.out.println("Respuesta: "
				+ respuestaServidor2.get_return().getResponse() + "		Usuario: "
				+ nombreUsuario3 + "	Pwd: " + contrasenaUsuario3);
		comprobarPrueba(false, respuestaServidor2.get_return().getResponse());

		// Cambiar contraseña de usuario que existe justo antes de borrarle
		System.out
				.println(contadorPruebas
						+ ". Cambiar contraseña de usuario que existe justo antes de borrarle -> OK");
		contadorPruebas++;
		user3 = new ChangePassword();
		respuestaServidor3 = new ChangePasswordResponse();

		pwdPair = new PasswordPair();
		pwdPair.setOldpwd(contrasenaUsuario3);
		contrasenaUsuario3 = "carapanhsivgkut";
		pwdPair.setNewpwd(contrasenaUsuario3);
		user3.setArgs0(pwdPair);

		respuestaServidor3 = stubServidor3.changePassword(user3);

		System.out.println("Respuesta: "
				+ respuestaServidor3.get_return().getResponse());
		comprobarPrueba(true, respuestaServidor3.get_return().getResponse());

		// Borrar usuario que no tiene cuentas
		System.out.println(contadorPruebas
				+ ". Borrar usuario que no tiene cuentas -> OK");
		contadorPruebas++;
		user2 = new RemoveUser();
		respuestaServidor2 = new RemoveUserResponse();

		username.setUsername(nombreUsuario3);
		user2.setArgs0(username);

		respuestaServidor2 = stubServidor1.removeUser(user2);

		System.out.println("Respuesta: "
				+ respuestaServidor2.get_return().getResponse() + "		Usuario: "
				+ nombreUsuario3 + "	Pwd: " + contrasenaUsuario3);
		comprobarPrueba(true, respuestaServidor2.get_return().getResponse());

		// Operacion de un usuario que ha sido borrado
		
		System.out.println(contadorPruebas
						+ ". Operacion de un usuario que ha sido borrado (Obtener movimientos) -> error");
		contadorPruebas++;
		ultimosMovimientos = new GetMyMovements();
		respuestaServidor8 = new GetMyMovementsResponse();
		respuestaServidor8 = stubServidor3.getMyMovements(ultimosMovimientos);

		System.out.println("Respuesta: "
				+ respuestaServidor8.get_return().getResult()
				+ "		Movimientos: "
				+ Arrays.toString(respuestaServidor8.get_return()
						.getMovementQuantities()));
		comprobarPrueba(false, respuestaServidor8.get_return().getResult());
		
		// Borrar usuario que si tiene cuentas
		System.out.println(contadorPruebas
				+ ". Borrar usuario que si tiene cuentas -> error");
		contadorPruebas++;
		user2 = new RemoveUser();
		respuestaServidor2 = new RemoveUserResponse();

		username.setUsername(nombreUsuario2);
		user2.setArgs0(username);

		respuestaServidor2 = stubServidor1.removeUser(user2);

		System.out.println("Respuesta: "
				+ respuestaServidor2.get_return().getResponse() + "		Usuario: "
				+ nombreUsuario2 + "	Pwd: " + contrasenaUsuario2);
		comprobarPrueba(false, respuestaServidor2.get_return().getResponse());

		// Cambiar contraseña de usuario que no existe porque esta en sesion
		// pero le ha borrado admin
		System.out
				.println(contadorPruebas
						+ ". Cambiar contraseña de usuario que no existe porque esta en sesion pero le ha borrado admin -> error");
		contadorPruebas++;
		user3 = new ChangePassword();
		respuestaServidor3 = new ChangePasswordResponse();

		pwdPair = new PasswordPair();
		pwdPair.setOldpwd(contrasenaUsuario3);
		pwdPair.setNewpwd("carapan09555");
		user3.setArgs0(pwdPair);

		respuestaServidor3 = stubServidor3.changePassword(user3);

		System.out.println("Respuesta: "
				+ respuestaServidor3.get_return().getResponse());
		comprobarPrueba(false, respuestaServidor3.get_return().getResponse());

		// Borrar usuario que no existe
		System.out.println(contadorPruebas
				+ ". Borrar usuario que no existe -> error");
		contadorPruebas++;
		user2 = new RemoveUser();
		respuestaServidor2 = new RemoveUserResponse();

		username.setUsername("perro");
		user2.setArgs0(username);

		respuestaServidor2 = stubServidor1.removeUser(user2);

		System.out.println("Respuesta: "
				+ respuestaServidor2.get_return().getResponse() + "		Usuario: "
				+ "perro" );
		comprobarPrueba(false, respuestaServidor2.get_return().getResponse());

		// Borrar admin a sí mismo
		System.out.println(contadorPruebas
				+ ". Borrar admin a sí mismo -> error");
		contadorPruebas++;
		user2 = new RemoveUser();
		respuestaServidor2 = new RemoveUserResponse();

		username.setUsername("admin");
		user2.setArgs0(username);

		respuestaServidor2 = stubServidor1.removeUser(user2);

		System.out.println("Respuesta: "
				+ respuestaServidor2.get_return().getResponse() + "		Usuario: "
				+ "admin");
		comprobarPrueba(false, respuestaServidor2.get_return().getResponse());

		// 2 Logouts en sesion 1
		System.out.println(contadorPruebas + ". 3 logouts sesion 1\n");
		contadorPruebas++;
		Logout logout = new Logout();
		stubServidor1.logout(logout);
		stubServidor1.logout(logout);
		stubServidor1.logout(logout);

		// Crear cliente con usuario de sesion 1
		System.out
				.println(contadorPruebas
						+ ". Crear un usuario cuando queda una sesion activa de admin -> OK");
		contadorPruebas++;
		randomString = Integer.toString(rand.nextInt(10000));
		String nombreUsuario4 = "evaC" + randomString;
		String contrasenaUsuario4;
		user = new AddUser();
		username = new Username();
		username.setUsername(nombreUsuario4);
		user.setArgs0(username);

		respuestaServidor = new AddUserResponseE();
		respuestaServidor = stubServidor1.addUser(user);

		contrasenaUsuario4 = respuestaServidor.get_return().getPwd();
		System.out.println("Respuesta: "
				+ respuestaServidor.get_return().getResponse() + "		Usuario: "
				+ nombreUsuario4 + "	Pwd: " + contrasenaUsuario4);
		comprobarPrueba(true, respuestaServidor.get_return().getResponse());

		// Ultimo logout sesion 1
		System.out.println(contadorPruebas + ". Ultimo logout sesion 1");
		contadorPruebas++;
		stubServidor1.logout(logout);

		// Crear un usuario cuando no quedan sesiones activas en sesion 1
		System.out
				.println(contadorPruebas
						+ ". Crear un usuario cuando no quedan sesiones activas en sesion 1 -> error");
		contadorPruebas++;
		randomString = Integer.toString(rand.nextInt(10000));
		String nombreUsuario5 = "evaC" + randomString;
		String contrasenaUsuario5;
		user = new AddUser();
		username = new Username();
		username.setUsername(nombreUsuario5);
		user.setArgs0(username);

		respuestaServidor = new AddUserResponseE();
		respuestaServidor = stubServidor1.addUser(user);

		contrasenaUsuario5 = respuestaServidor.get_return().getPwd();
		System.out.println("Respuesta: "
				+ respuestaServidor.get_return().getResponse() + "		Usuario: "
				+ nombreUsuario5 + "	Pwd: " + contrasenaUsuario5);
		comprobarPrueba(false, respuestaServidor.get_return().getResponse());

		// Login con usuario valido en cliente 1 que ya no tiene ninguna sesion
		System.out
				.println(contadorPruebas
						+ ". Login con usuario valido en cliente 1 que ya no tiene ninguna sesion -> OK");
		contadorPruebas++;
		contenedorPeticionServidor = new Login();
		respuestaBackEnd = new LoginResponse();

		userLogin = new User();
		userLogin.setName(nombreUsuario1);
		userLogin.setPwd(contrasenaNueva1);
		contenedorPeticionServidor.setArgs0(userLogin);

		respuestaBackEnd = stubServidor1.login(contenedorPeticionServidor);
		System.out.println("Login: "
				+ respuestaBackEnd.get_return().getResponse());
		comprobarPrueba(true, respuestaBackEnd.get_return().getResponse());

		// Logout en sesion 1
		System.out.println(contadorPruebas + ". Logout sesion 1\n");
		contadorPruebas++;
		logout = new Logout();
		stubServidor1.logout(logout);

		// 2 Logouts en sesion 2
		System.out.println(contadorPruebas + ". Se hacen 2 logouts sesion 2\n");
		contadorPruebas++;
		logout = new Logout();
		stubServidor2.logout(logout);
		stubServidor2.logout(logout);

		// 1 Logout en sesion 3
		System.out.println(contadorPruebas + ". Logout sesion 3\n");
		contadorPruebas++;
		logout = new Logout();
		stubServidor3.logout(logout);
		stubServidor3.logout(logout);

		// Logout en sesion sin que haya ninguna sesion. No tiene que hacer nada
		System.out
				.println(contadorPruebas
						+ ". Logout en sesion sin que haya ninguna sesion. No tiene que hacer nada\n");
		contadorPruebas++;
		logout = new Logout();
		stubServidor3.logout(logout);

		// Login de usuario que movimientos en otro cliente distinto para ver su
		// persistencia despues de haber hecho logut en todas las sesiones
		System.out
				.println(contadorPruebas
						+ ". Login de usuario que movimientos en otro cliente distinto para ver su persistencia despues de haber hecho logut en todas las sesiones -> OK");
		contadorPruebas++;
		contenedorPeticionServidor = new Login();
		respuestaBackEnd = new LoginResponse();

		userLogin = new User();
		userLogin.setName("admin");
		userLogin.setPwd("admin");
		contenedorPeticionServidor.setArgs0(userLogin);

		respuestaBackEnd = stubServidor2.login(contenedorPeticionServidor);
		System.out.println("Login: "
				+ respuestaBackEnd.get_return().getResponse());
		comprobarPrueba(true, respuestaBackEnd.get_return().getResponse());

		// Obtener movimientos de un cliente con menos de 10 movimientos
		System.out
				.println(contadorPruebas
						+ ". Obtener movimientos de un cliente con menos de 10 movimientos -> OK");
		contadorPruebas++;
		ultimosMovimientos = new GetMyMovements();
		respuestaServidor8 = new GetMyMovementsResponse();
		respuestaServidor8 = stubServidor2.getMyMovements(ultimosMovimientos);

		System.out.println("Respuesta: "
				+ respuestaServidor8.get_return().getResult()
				+ "		Movimientos: "
				+ Arrays.toString(respuestaServidor8.get_return()
						.getMovementQuantities()));
		comprobarPrueba(true, respuestaServidor8.get_return().getResult());

		// 1 Logout en sesion 2
		System.out.println(contadorPruebas + ". Logout sesion 3\n");
		contadorPruebas++;
		logout = new Logout();
		stubServidor2.logout(logout);

		stubServidor1.cleanup();
		stubServidor2.cleanup();
		stubServidor3.cleanup();
		stubServidor4.cleanup();
		stubServidor5.cleanup();
		stubServidor6.cleanup();
		stubServidor7.cleanup();
		stubServidor8.cleanup();
		stubServidor9.cleanup();

		System.out.println("PRUEBAS ERRÓNEAS: " + pruebasErroneas);

	}
}
