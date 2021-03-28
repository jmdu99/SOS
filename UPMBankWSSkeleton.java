/**
 * UPMBankWSSkeleton.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */
package es.upm.fi.sos.upmbank;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;

import es.upm.fi.sos.upmbank.xsd.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * UPMBankWSSkeleton java skeleton for the axisService
 */
public class UPMBankWSSkeleton {

	private static Map<String, String> clientes = new HashMap<String, String>();
	private static Map<String, ArrayList<BankAccount>> cuentas = new HashMap<String, ArrayList<BankAccount>>();
	private static ArrayList<Movement> movimientos = new ArrayList<Movement>();
	private static Map<String, Double> saldos = new HashMap<String, Double>();
	private static String adminName = "admin";
	private static String adminPwd = "admin";
	private static int ultimoNumCuenta = 0;

	private int sesiones;
	private boolean sesionActiva;
	private User clienteActivo;
	private String contrasenaActiva;

	public UPMBankWSSkeleton() {
		System.out.println("Crea instancia UPMBank");
		this.sesionActiva = false;
		this.clienteActivo = new User();
		contrasenaActiva = "";
		clientes.put(adminName, adminPwd);
		sesiones = 0;
	}

	private boolean clienteExiste(String username) {
		if (clientes.containsKey(username)) {
			return true; 
		} else {
			return false;
		}
	}
	
	private boolean cuentaPerteneceUsuario(String username, String iban) {
		ArrayList<BankAccount> cuentasUsuario = cuentas.get(username);
		boolean encontrado = false;
		if (cuentasUsuario != null) {
			int i = 0;
			while (!encontrado && i<cuentasUsuario.size()){
				if (cuentasUsuario.get(i).getIBAN().equals(iban)){
					encontrado = true;
				}
				i++;
			}
		}
		return encontrado;
	}

	/**
	 * Auto generated method signature
	 * 
	 * @param addBankAcc
	 * @return addBankAccResponse
	 */

	public es.upm.fi.sos.upmbank.AddBankAccResponse addBankAcc(
			es.upm.fi.sos.upmbank.AddBankAcc addBankAcc) {
		// TODO : fill this with the necessary business logic

		es.upm.fi.sos.upmbank.xsd.BankAccountResponse respuestaCliente = new es.upm.fi.sos.upmbank.xsd.BankAccountResponse();
		AddBankAccResponse contenedorRespuestaCliente = new AddBankAccResponse();

		Double cantidad = addBankAcc.getArgs0().getQuantity();

		if (sesionActiva && cantidad >= 0 && clienteExiste(clienteActivo.getName())) {
			BankAccount account = new BankAccount();
			ultimoNumCuenta++;
			String ultimoNumCuentaString = Integer.toString(ultimoNumCuenta);
			account.setIBAN(ultimoNumCuentaString);
			if (cuentas.containsKey(clienteActivo.getName())) {
				cuentas.get(clienteActivo.getName()).add(account);
				saldos.put(ultimoNumCuentaString, cantidad);
			} else {
				ArrayList<BankAccount> listaCuentasCliente = new ArrayList<BankAccount>();
				listaCuentasCliente.add(account);
				cuentas.put(clienteActivo.getName(), listaCuentasCliente);
				saldos.put(ultimoNumCuentaString, cantidad);
			}
			respuestaCliente.setResult(true);
			respuestaCliente.setIBAN(ultimoNumCuentaString);
			contenedorRespuestaCliente.set_return(respuestaCliente);

		} else {
			respuestaCliente.setResult(false);
			contenedorRespuestaCliente.set_return(respuestaCliente);
		}
		return contenedorRespuestaCliente;
	}

	/**
	 * Auto generated method signature
	 * 
	 * @param closeBankAcc
	 * @return closeBankAccResponse
	 */

	public es.upm.fi.sos.upmbank.CloseBankAccResponse closeBankAcc(
			es.upm.fi.sos.upmbank.CloseBankAcc closeBankAcc) {
		// TODO : fill this with the necessary business logic

		es.upm.fi.sos.upmbank.xsd.Response respuestaCliente = new es.upm.fi.sos.upmbank.xsd.Response();
		CloseBankAccResponse contenedorRespuestaCliente = new CloseBankAccResponse();

		String ibanString = closeBankAcc.getArgs0().getIBAN();
		boolean cuentaPerteneceClienteActivo = false;

		if (sesionActiva && saldos.get(ibanString) == 0
				&& clienteExiste(clienteActivo.getName())) {

			ArrayList<BankAccount> listaCuentasClienteActivo = cuentas
					.get(clienteActivo.getName());
			if (listaCuentasClienteActivo != null) {
				int i = 0;
				while (!cuentaPerteneceClienteActivo
						&& i < listaCuentasClienteActivo.size()) {
					BankAccount account = listaCuentasClienteActivo.get(i);
					if (account.getIBAN().equals(ibanString)) {
						cuentaPerteneceClienteActivo = true;
					}
					i++;
				}
				if (cuentaPerteneceClienteActivo) {
					saldos.remove(ibanString);
					listaCuentasClienteActivo.remove(i - 1);
					respuestaCliente.setResponse(true);
					contenedorRespuestaCliente.set_return(respuestaCliente);
				} else {
					respuestaCliente.setResponse(false);
					contenedorRespuestaCliente.set_return(respuestaCliente);
				}

			} else {
				respuestaCliente.setResponse(false);
				contenedorRespuestaCliente.set_return(respuestaCliente);
			}
		} else {
			respuestaCliente.setResponse(false);
			contenedorRespuestaCliente.set_return(respuestaCliente);
		}
		return contenedorRespuestaCliente;
	}

	/**
	 * Auto generated method signature
	 * 
	 * @param logout
	 * @return
	 */

	public void logout(es.upm.fi.sos.upmbank.Logout logout) {
		// TODO : fill this with the necessary business logic
		if (sesionActiva) {
			sesiones--;
			if (sesiones == 0) {
				sesionActiva = false;
				clienteActivo = new User();
			}
		}
	}

	/**
	 * Auto generated method signature
	 * 
	 * @param removeUser
	 * @return removeUserResponse
	 * @throws RemoteException
	 */

	public es.upm.fi.sos.upmbank.RemoveUserResponse removeUser(
			es.upm.fi.sos.upmbank.RemoveUser removeUser) throws RemoteException {

		es.upm.fi.sos.upmbank.xsd.Response respuestaCliente = new es.upm.fi.sos.upmbank.xsd.Response();
		RemoveUserResponse contenedorRespuestaCliente = new RemoveUserResponse();

		UPMAuthenticationAuthorizationWSSkeletonStub.RemoveUser userBackEnd = new UPMAuthenticationAuthorizationWSSkeletonStub.RemoveUser();
		UPMAuthenticationAuthorizationWSSkeletonStub.RemoveUserE contenedorPeticionLDAP = new UPMAuthenticationAuthorizationWSSkeletonStub.RemoveUserE();
		UPMAuthenticationAuthorizationWSSkeletonStub.RemoveUserResponseE respuestaBackEnd = new UPMAuthenticationAuthorizationWSSkeletonStub.RemoveUserResponseE();

		if (sesionActiva && clienteActivo.getName().equals(adminName)) {
			if (cuentas.containsKey(removeUser.getArgs0().getUsername())) {
				respuestaCliente.setResponse(false);
				contenedorRespuestaCliente.set_return(respuestaCliente);
			} else {
				if (removeUser.getArgs0().getUsername().equals(adminName)) {
					respuestaCliente.setResponse(false);
					contenedorRespuestaCliente.set_return(respuestaCliente);
				} else {
					userBackEnd.setName(removeUser.getArgs0().getUsername());
					String contrasena = clientes.get(removeUser.getArgs0()
							.getUsername());
					userBackEnd.setPassword(contrasena);
					contenedorPeticionLDAP.setRemoveUser(userBackEnd);
					UPMAuthenticationAuthorizationWSSkeletonStub stubLDAP = new UPMAuthenticationAuthorizationWSSkeletonStub();

					respuestaBackEnd = stubLDAP
							.removeUser(contenedorPeticionLDAP);

					if (respuestaBackEnd.get_return().getResult()) {
						clientes.remove(removeUser.getArgs0().getUsername());
						respuestaCliente.setResponse(true);
						contenedorRespuestaCliente.set_return(respuestaCliente);
					} else {
						respuestaCliente.setResponse(false);
						contenedorRespuestaCliente.set_return(respuestaCliente);
					}
				}
			}
		} else {
			respuestaCliente.setResponse(false);
			contenedorRespuestaCliente.set_return(respuestaCliente);
		}
		return contenedorRespuestaCliente;

	}

	/**
	 * Auto generated method signature
	 * 
	 * @param addWithdrawal
	 * @return addWithdrawalResponse
	 */

	public es.upm.fi.sos.upmbank.AddWithdrawalResponse addWithdrawal(
			es.upm.fi.sos.upmbank.AddWithdrawal addWithdrawal) {
		// TODO : fill this with the necessary business logic

		es.upm.fi.sos.upmbank.xsd.AddMovementResponse respuestaCliente = new es.upm.fi.sos.upmbank.xsd.AddMovementResponse();
		AddWithdrawalResponse contenedorRespuestaCliente = new AddWithdrawalResponse();

		String ibanString = addWithdrawal.getArgs0().getIBAN();
		Double cantidad = addWithdrawal.getArgs0().getQuantity();

		boolean cuentaPerteneceClienteActivo = false;

		if (sesionActiva && clienteExiste(clienteActivo.getName())
				&& cantidad > 0) {

			ArrayList<BankAccount> listaCuentasClienteActivo = cuentas
					.get(clienteActivo.getName());
			if (listaCuentasClienteActivo != null) {
				int i = 0;
				while (!cuentaPerteneceClienteActivo
						&& i < listaCuentasClienteActivo.size()) {
					BankAccount account = listaCuentasClienteActivo.get(i);
					if (account.getIBAN().equals(ibanString)) {
						cuentaPerteneceClienteActivo = true;
					}
					i++;
				}
				if (cuentaPerteneceClienteActivo) {
					Double saldo = saldos.get(ibanString);
					if (cantidad <= saldo) {
						saldo -= cantidad;
						saldos.put(ibanString, saldo);
						respuestaCliente.setBalance(saldo);
						Movement mov = new Movement();
						mov.setIBAN(ibanString);
						mov.setQuantity(cantidad);
						movimientos.add(mov);

						respuestaCliente.setResult(true);
						contenedorRespuestaCliente.set_return(respuestaCliente);
					} else {
						respuestaCliente.setResult(false);
						contenedorRespuestaCliente.set_return(respuestaCliente);
					}
					
				} else {
					respuestaCliente.setResult(false);
					contenedorRespuestaCliente.set_return(respuestaCliente);
				}

			} else {
				respuestaCliente.setResult(false);
				contenedorRespuestaCliente.set_return(respuestaCliente);
			}
		} else {
			respuestaCliente.setResult(false);
			contenedorRespuestaCliente.set_return(respuestaCliente);
		}
		return contenedorRespuestaCliente;

	}

	/**
	 * Auto generated method signature
	 * 
	 * @param addUser
	 * @return addUserResponse
	 * @throws RemoteException
	 */

	public es.upm.fi.sos.upmbank.AddUserResponse addUser(
			es.upm.fi.sos.upmbank.AddUser addUser) throws RemoteException {
		// TODO : fill this with the necessary business logic

		UPMAuthenticationAuthorizationWSSkeletonStub stubLDAP = new UPMAuthenticationAuthorizationWSSkeletonStub();

		es.upm.fi.sos.upmbank.xsd.AddUserResponse respuestaCliente = new es.upm.fi.sos.upmbank.xsd.AddUserResponse();
		AddUserResponse contenedorRespuestaCliente = new AddUserResponse();

		UPMAuthenticationAuthorizationWSSkeletonStub.UserBackEnd userBackEnd = new UPMAuthenticationAuthorizationWSSkeletonStub.UserBackEnd();
		UPMAuthenticationAuthorizationWSSkeletonStub.AddUser contenedorPeticionLDAP = new UPMAuthenticationAuthorizationWSSkeletonStub.AddUser();
		UPMAuthenticationAuthorizationWSSkeletonStub.AddUserResponse respuestaBackEnd = new UPMAuthenticationAuthorizationWSSkeletonStub.AddUserResponse();

		if (sesionActiva && clienteActivo.getName().equals(adminName)) {
			userBackEnd.setName(addUser.getArgs0().getUsername());
			contenedorPeticionLDAP.setUser(userBackEnd);
			respuestaBackEnd = stubLDAP.addUser(contenedorPeticionLDAP);

			if (respuestaBackEnd.get_return().getResult()) {

				clientes.put(addUser.getArgs0().getUsername(), respuestaBackEnd
						.get_return().getPassword());

				respuestaCliente.setResponse(true);
				respuestaCliente.setPwd(respuestaBackEnd.get_return()
						.getPassword());
				contenedorRespuestaCliente.set_return(respuestaCliente);
			} else {
				respuestaCliente.setResponse(false);
				contenedorRespuestaCliente.set_return(respuestaCliente);
			}

		} else {
			respuestaCliente.setResponse(false);
			contenedorRespuestaCliente.set_return(respuestaCliente);
		}
		return contenedorRespuestaCliente;
	}

	/**
	 * Auto generated method signature
	 * 
	 * @param addIncome
	 * @return addIncomeResponse
	 */

	public es.upm.fi.sos.upmbank.AddIncomeResponse addIncome(
			es.upm.fi.sos.upmbank.AddIncome addIncome) {
		// TODO : fill this with the necessary business logic

		es.upm.fi.sos.upmbank.xsd.AddMovementResponse respuestaCliente = new es.upm.fi.sos.upmbank.xsd.AddMovementResponse();
		AddIncomeResponse contenedorRespuestaCliente = new AddIncomeResponse();

		String ibanString = addIncome.getArgs0().getIBAN();
		Double cantidad = addIncome.getArgs0().getQuantity();

		boolean cuentaPerteneceClienteActivo = false;

		if (sesionActiva && clienteExiste(clienteActivo.getName())
				&& cantidad > 0) {

			ArrayList<BankAccount> listaCuentasClienteActivo = cuentas
					.get(clienteActivo.getName());
			if (listaCuentasClienteActivo != null) {
				int i = 0;
				while (!cuentaPerteneceClienteActivo
						&& i < listaCuentasClienteActivo.size()) {
					BankAccount account = listaCuentasClienteActivo.get(i);
					if (account.getIBAN().equals(ibanString)) {
						cuentaPerteneceClienteActivo = true;
					}
					i++;
				}
				if (cuentaPerteneceClienteActivo) {
					Double saldo = saldos.get(ibanString);
					saldo += cantidad;
					saldos.put(ibanString, saldo);
					respuestaCliente.setBalance(saldo);
					Movement mov = new Movement();
					mov.setIBAN(ibanString);
					mov.setQuantity(cantidad);
					movimientos.add(mov);

					respuestaCliente.setResult(true);
					contenedorRespuestaCliente.set_return(respuestaCliente);
				} else {
					respuestaCliente.setResult(false);
					contenedorRespuestaCliente.set_return(respuestaCliente);
				}

			} else {
				respuestaCliente.setResult(false);
				contenedorRespuestaCliente.set_return(respuestaCliente);
			}
		} else {
			respuestaCliente.setResult(false);
			contenedorRespuestaCliente.set_return(respuestaCliente);
		}
		return contenedorRespuestaCliente;

	}

	/**
	 * Auto generated method signature
	 * 
	 * @param login
	 * @return loginResponse
	 * @throws RemoteException
	 */

	public es.upm.fi.sos.upmbank.LoginResponse login(
			es.upm.fi.sos.upmbank.Login login) throws RemoteException {
		// TODO : fill this with the necessary business logic

		UPMAuthenticationAuthorizationWSSkeletonStub stubLDAP = new UPMAuthenticationAuthorizationWSSkeletonStub();

		es.upm.fi.sos.upmbank.xsd.Response respuestaCliente = new es.upm.fi.sos.upmbank.xsd.Response();
		LoginResponse contenedorRespuestaCliente = new LoginResponse();

		UPMAuthenticationAuthorizationWSSkeletonStub.LoginBackEnd loginBackEnd = new UPMAuthenticationAuthorizationWSSkeletonStub.LoginBackEnd();
		UPMAuthenticationAuthorizationWSSkeletonStub.Login contenedorPeticionLDAP = new UPMAuthenticationAuthorizationWSSkeletonStub.Login();
		UPMAuthenticationAuthorizationWSSkeletonStub.LoginResponse respuestaBackEnd = new UPMAuthenticationAuthorizationWSSkeletonStub.LoginResponse();

		// Acceso de adnin sin consultar al ldap

		if (login.getArgs0().getName().equals(adminName)) {
			if (sesionActiva) {
				if (clienteActivo.getName().equals(adminName)) {
					sesiones++;
					respuestaCliente.setResponse(true);
					contenedorRespuestaCliente.set_return(respuestaCliente);
				} else {
					respuestaCliente.setResponse(false);
					contenedorRespuestaCliente.set_return(respuestaCliente);
				}
			} else {
				if (login.getArgs0().getPwd().equals(adminPwd)) {
					sesionActiva = true;
					contrasenaActiva = login.getArgs0().getPwd();
					clienteActivo.setName(login.getArgs0().getName());
					clienteActivo.setPwd(login.getArgs0().getPwd());
					sesiones++;
					respuestaCliente.setResponse(true);
					contenedorRespuestaCliente.set_return(respuestaCliente);
				} else {
					respuestaCliente.setResponse(false);
					contenedorRespuestaCliente.set_return(respuestaCliente);
				}
			}
		} else {
			loginBackEnd.setName(login.getArgs0().getName());
			loginBackEnd.setPassword(login.getArgs0().getPwd());
			contenedorPeticionLDAP.setLogin(loginBackEnd);
			respuestaBackEnd = stubLDAP.login(contenedorPeticionLDAP);

			// Si el usuario tiene sesion activa, devuelvo true sin comprobar
			// contraseña si el usuario que quiere hacer login es el mismo
			if (sesionActiva
					&& clienteActivo.getName().equals(
							login.getArgs0().getName())) {
				sesiones++;
				respuestaCliente.setResponse(true);
				contenedorRespuestaCliente.set_return(respuestaCliente);
			} else if (!sesionActiva
					&& respuestaBackEnd.get_return().getResult()) {
				// Si no hay sesión activa y el usuario y contraseña son
				// correctos,
				// creamos la sesión
				sesionActiva = true;
				contrasenaActiva = login.getArgs0().getPwd();
				clienteActivo.setName(login.getArgs0().getName());
				clienteActivo.setPwd(login.getArgs0().getPwd());
				sesiones++;
				respuestaCliente.setResponse(true);
				contenedorRespuestaCliente.set_return(respuestaCliente);
			} else {
				// Autenticación incorrecta
				respuestaCliente.setResponse(false);
				contenedorRespuestaCliente.set_return(respuestaCliente);
			}
		}
		return contenedorRespuestaCliente;
	}

	/**
	 * Auto generated method signature
	 * 
	 * @param getMyMovements
	 * @return getMyMovementsResponse
	 */

	public es.upm.fi.sos.upmbank.GetMyMovementsResponse getMyMovements(
			es.upm.fi.sos.upmbank.GetMyMovements getMyMovements) {
		// TODO : fill this with the necessary business logic
	
		GetMyMovementsResponse contenedorRespuestaCliente = new GetMyMovementsResponse();

		MovementList movementList = new MovementList();
		double [] ultimosMovimientos = new double[10];

		if (sesionActiva && clienteExiste(clienteActivo.getName())) {
			int i = movimientos.size();
			int movimientosMetidos = 0;
			while (i > 0 && movimientosMetidos < 10) {
				if (cuentaPerteneceUsuario(clienteActivo.getName(),movimientos.get(i-1).getIBAN())) {
					ultimosMovimientos[movimientosMetidos] = movimientos.get(i-1).getQuantity();
					movimientosMetidos++;
				}
				i--;
			}
			double [] ultimosMovimientosCorto = new double[movimientosMetidos];
			i = 0;
			while (i < movimientosMetidos) {
				ultimosMovimientosCorto[i] = ultimosMovimientos[i];
				i++;
			}
			
			movementList.setMovementQuantities(ultimosMovimientosCorto);
			movementList.setResult(true);
			contenedorRespuestaCliente.set_return(movementList);
			
		} else {
			movementList.setResult(false);
			contenedorRespuestaCliente.set_return(movementList);
		}
		
		
		return contenedorRespuestaCliente;
	}

	/**
	 * Auto generated method signature
	 * 
	 * @param changePassword
	 * @return changePasswordResponse
	 * @throws RemoteException
	 */

	public es.upm.fi.sos.upmbank.ChangePasswordResponse changePassword(
			es.upm.fi.sos.upmbank.ChangePassword changePassword)
			throws RemoteException {
		// TODO : fill this with the necessary business logic

		UPMAuthenticationAuthorizationWSSkeletonStub stubLDAP = new UPMAuthenticationAuthorizationWSSkeletonStub();

		es.upm.fi.sos.upmbank.xsd.Response respuestaCliente = new es.upm.fi.sos.upmbank.xsd.Response();
		ChangePasswordResponse contenedorRespuestaCliente = new ChangePasswordResponse();

		UPMAuthenticationAuthorizationWSSkeletonStub.ChangePasswordBackEnd changePwdBackEnd = new UPMAuthenticationAuthorizationWSSkeletonStub.ChangePasswordBackEnd();
		UPMAuthenticationAuthorizationWSSkeletonStub.ChangePassword contenedorPeticionLDAP = new UPMAuthenticationAuthorizationWSSkeletonStub.ChangePassword();
		UPMAuthenticationAuthorizationWSSkeletonStub.ChangePasswordResponseE respuestaBackEnd = new UPMAuthenticationAuthorizationWSSkeletonStub.ChangePasswordResponseE();

		if (sesionActiva && clienteExiste(clienteActivo.getName())) {
			if (clienteActivo.getName().equals(adminName)) {
				if (changePassword.getArgs0().getOldpwd()
						.equals(contrasenaActiva)) {
					adminPwd = changePassword.getArgs0().getNewpwd();
					contrasenaActiva = adminPwd;
					respuestaCliente.setResponse(true);
					contenedorRespuestaCliente.set_return(respuestaCliente);
				} else {
					respuestaCliente.setResponse(false);
					contenedorRespuestaCliente.set_return(respuestaCliente);
				}
			} else {
				if (changePassword.getArgs0().getOldpwd()
						.equals(contrasenaActiva)) {
					changePwdBackEnd.setName(clienteActivo.getName());
					String contrasenaReal = clientes.get(clienteActivo
							.getName());
					changePwdBackEnd.setOldpwd(contrasenaReal);
					changePwdBackEnd.setNewpwd(changePassword.getArgs0()
							.getNewpwd());

					contenedorPeticionLDAP.setChangePassword(changePwdBackEnd);

					respuestaBackEnd = stubLDAP
							.changePassword(contenedorPeticionLDAP);

					if (respuestaBackEnd.get_return().getResult()) {
						contrasenaActiva = changePassword.getArgs0()
								.getNewpwd();
						clientes.put(clienteActivo.getName(), changePassword
								.getArgs0().getNewpwd());
						respuestaCliente.setResponse(true);
						contenedorRespuestaCliente.set_return(respuestaCliente);
					} else {
						respuestaCliente.setResponse(false);
						contenedorRespuestaCliente.set_return(respuestaCliente);
					}
				} else {
					respuestaCliente.setResponse(false);
					contenedorRespuestaCliente.set_return(respuestaCliente);
				}

			}
		} else {
			respuestaCliente.setResponse(false);
			contenedorRespuestaCliente.set_return(respuestaCliente);
		}
		return contenedorRespuestaCliente;
	}

}
