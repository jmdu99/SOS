
/**
 * ExtensionMapper.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:34:40 IST)
 */

        
            package es.upm.fi.sos.upmbank.xsd;
        
            /**
            *  ExtensionMapper class
            */
            @SuppressWarnings({"unchecked","unused"})
        
        public  class ExtensionMapper{

          public static java.lang.Object getTypeObject(java.lang.String namespaceURI,
                                                       java.lang.String typeName,
                                                       javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{

              
                  if (
                  "http://upmbank.sos.fi.upm.es/xsd".equals(namespaceURI) &&
                  "Username".equals(typeName)){
                   
                            return  es.upm.fi.sos.upmbank.xsd.Username.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://upmbank.sos.fi.upm.es/xsd".equals(namespaceURI) &&
                  "Movement".equals(typeName)){
                   
                            return  es.upm.fi.sos.upmbank.xsd.Movement.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://upmbank.sos.fi.upm.es/xsd".equals(namespaceURI) &&
                  "User".equals(typeName)){
                   
                            return  es.upm.fi.sos.upmbank.xsd.User.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://upmbank.sos.fi.upm.es/xsd".equals(namespaceURI) &&
                  "PasswordPair".equals(typeName)){
                   
                            return  es.upm.fi.sos.upmbank.xsd.PasswordPair.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://upmbank.sos.fi.upm.es/xsd".equals(namespaceURI) &&
                  "Response".equals(typeName)){
                   
                            return  es.upm.fi.sos.upmbank.xsd.Response.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://upmbank.sos.fi.upm.es/xsd".equals(namespaceURI) &&
                  "Deposit".equals(typeName)){
                   
                            return  es.upm.fi.sos.upmbank.xsd.Deposit.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://upmbank.sos.fi.upm.es/xsd".equals(namespaceURI) &&
                  "MovementList".equals(typeName)){
                   
                            return  es.upm.fi.sos.upmbank.xsd.MovementList.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://upmbank.sos.fi.upm.es/xsd".equals(namespaceURI) &&
                  "BankAccountResponse".equals(typeName)){
                   
                            return  es.upm.fi.sos.upmbank.xsd.BankAccountResponse.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://upmbank.sos.fi.upm.es/xsd".equals(namespaceURI) &&
                  "BankAccount".equals(typeName)){
                   
                            return  es.upm.fi.sos.upmbank.xsd.BankAccount.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://upmbank.sos.fi.upm.es/xsd".equals(namespaceURI) &&
                  "AddMovementResponse".equals(typeName)){
                   
                            return  es.upm.fi.sos.upmbank.xsd.AddMovementResponse.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://upmbank.sos.fi.upm.es/xsd".equals(namespaceURI) &&
                  "AddUserResponse".equals(typeName)){
                   
                            return  es.upm.fi.sos.upmbank.xsd.AddUserResponse.Factory.parse(reader);
                        

                  }

              
             throw new org.apache.axis2.databinding.ADBException("Unsupported type " + namespaceURI + " " + typeName);
          }

        }
    