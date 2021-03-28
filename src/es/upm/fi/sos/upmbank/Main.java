package es.upm.fi.sos.upmbank;

import java.rmi.RemoteException;

import es.upm.fi.sos.upmbank.*;
import es.upm.fi.sos.upmbank.xsd.*;

public class Main {

	public static void main(String[] args) throws RemoteException {

		String nombreUsuario = "eva00345";
		AddUser user = new AddUser();
		Username username = new Username();
		username.setUsername(nombreUsuario);
		user.setArgs0(username);
	
		UPMBankWSSkeleton upmBank = new UPMBankWSSkeleton();
		
		AddUserResponse res = new AddUserResponse();
		
		res = upmBank.addUser(user);
		System.out.println("Usuario: " + res.get_return().getResponse() + "	Pwd: "  + res.get_return().getPwd());

		
//		Login contenedorPeticionLDAP = new Login();
//		LoginResponse respuestaBackEnd = new LoginResponse();
//
//		User userLogin = new User();
//		userLogin.setName(nombreUsuario);
//		userLogin.setPwd(res.get_return().getPwd());
//		contenedorPeticionLDAP.setArgs0(userLogin);	
//		
//		respuestaBackEnd = upmBank.login(contenedorPeticionLDAP);
//		
//		System.out.println("Login: " + respuestaBackEnd.get_return().getResponse());
//		
		System.out.println("90. Borrar usuario que no tiene cuentas -> OK");

		RemoveUser user2 = new RemoveUser();
		RemoveUserResponse respuestaServidor2 = new RemoveUserResponse();
		
		username.setUsername(nombreUsuario);
		user2.setArgs0(username);
		
		respuestaServidor2 = upmBank.removeUser(user2);


		System.out.println("Respuesta: "
				+ respuestaServidor2.get_return().getResponse() );

		
	}
	
}
