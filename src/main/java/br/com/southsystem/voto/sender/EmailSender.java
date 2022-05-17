package br.com.southsystem.voto.sender;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import br.com.southsystem.voto.dto.PautaDto;
import br.com.southsystem.voto.dto.VotoConsultDto;

public class EmailSender {

	static Logger logger = Logger.getLogger(EmailSender.class);

	private static Properties getGmailConfig() throws Exception {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		return props;
	}

	private static Session getSessionConfig() throws Exception {
		return Session.getDefaultInstance(getGmailConfig(), new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("gustavo.south.system@gmail.com", "Admin@123");
			}
		});
	}
	
	private static String getEmailMessage(VotoConsultDto votoDto, PautaDto pautaDto) throws Exception {
	    return String.format("<body>"
	      	    			+ "	<center>"
	      	    			+ "		<h1>Votação sobre a Pauta: \"%s\" encerrada!</h1>"
	      	    			+ "		<p>Obrigado por votar %s!</p>"
	      	    			+ "     <p>Você votou por \"%s\"</p>"
	      	    			+ "		<p>Resultado: %s</p>"
	      	    			+ "     <p>Votos a favor: %s | Votos contra: %s</p>"
	      	    			+ "	</center>"
	      	    			+ "</body>", votoDto.getPautaNome(), 
	      	    						 votoDto.getAssociadoNome(), 
	      	    						 votoDto.getVotou(),
	      	    						 pautaDto.getResultado(),
	      	    						 pautaDto.getQtdVotosPositivos(),
	      	    						 pautaDto.getQtdVotosNegativos());
	}
	
	public static void emailSender(VotoConsultDto votoDto, PautaDto pautaDto) throws Exception {
		Message message = null;
		try {
			message = new MimeMessage(getSessionConfig());
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(votoDto.getAssociadoEmail()));
			message.setSubject("Votação encerrada!");
			message.setContent(getEmailMessage(votoDto, pautaDto), "text/html; charset=utf-8");
			Transport.send(message);
		} catch (Exception e) {
			logger.error(e);
			throw (e);
		}
	}
}
