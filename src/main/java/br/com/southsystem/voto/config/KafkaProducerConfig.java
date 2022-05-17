package br.com.southsystem.voto.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import br.com.southsystem.voto.constants.Constants;
import br.com.southsystem.voto.dto.AssembleiaConfigureDto;

@Configuration
public class KafkaProducerConfig {

	public static boolean isAssembleiaConfirured;
	public static Integer duration;
	public static int pautaId;
	
	public static void openAssembleia(AssembleiaConfigureDto configDto) {
		KafkaProducerConfig.isAssembleiaConfirured = true;
		KafkaProducerConfig.duration = configDto.getDurationInMs();
		KafkaProducerConfig.pautaId  = configDto.getPautaId();
	}

	public static void closeAssembleia() {
		KafkaProducerConfig.isAssembleiaConfirured = false;
		KafkaProducerConfig.duration = 0;
		KafkaProducerConfig.pautaId  = 0;
	}
	
	private Map<String, Object> producerConfig() {
		Map<String, Object> props = new HashMap<String, Object>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Constants.BOOSTRAP_SERVERS);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		props.put(ProducerConfig.LINGER_MS_CONFIG, duration == null? 60000:duration);
		return props;
	}
	
	private ProducerFactory<String, Object> producerFactory() {
		return new DefaultKafkaProducerFactory<String, Object>(producerConfig());
	}
	
	public KafkaTemplate<String, Object> kafkaTemplate() {
		return new KafkaTemplate<String, Object>(producerFactory());
	}
}
