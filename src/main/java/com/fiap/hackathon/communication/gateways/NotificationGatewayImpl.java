package com.fiap.hackathon.communication.gateways;

import com.fiap.fastfood.common.exceptions.custom.CustomerNotificationException;
import com.fiap.fastfood.common.exceptions.custom.ExceptionCodes;
import com.fiap.fastfood.common.interfaces.gateways.NotificationGateway;
import com.fiap.fastfood.common.interfaces.gateways.OrquestrationGateway;
import com.fiap.fastfood.common.logging.LoggingPattern;
import com.fiap.fastfood.common.logging.TransactionInformationStorage;
import com.fiap.fastfood.core.entity.OrquestrationStepEnum;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static com.fiap.fastfood.common.logging.Constants.MAX_RECEIVE_COUNT;

public class NotificationGatewayImpl implements NotificationGateway {

    private static final Logger logger = LogManager.getLogger(NotificationGatewayImpl.class);

    @Override
    public void notifyCustomer(Long customerId, OrquestrationStepEnum step, OrquestrationGateway orquestrationGateway) throws CustomerNotificationException {
        logger.info(
                LoggingPattern.NOTIFICATION_INIT_LOG,
                TransactionInformationStorage.getSagaId(),
                step.name(),
                customerId
        );

        try {

            //TODO implementar algum tipo de envio de e-mail
            Thread.sleep(2000);

            orquestrationGateway.sendResponse(
                    TransactionInformationStorage.getOrderId(),
                    customerId,
                    TransactionInformationStorage.getPaymentId(),
                    OrquestrationStepEnum.NOTIFY_CUSTOMER,
                    Boolean.TRUE
            );

            logger.info(
                    LoggingPattern.NOTIFICATION_END_LOG,
                    TransactionInformationStorage.getSagaId()
            );

        } catch (Exception ex) {

            logger.error(
                    LoggingPattern.NOTIFICATION_ERROR_LOG,
                    TransactionInformationStorage.getSagaId(),
                    ex.getMessage()
            );

            var receiveCount = Integer.valueOf(TransactionInformationStorage.getReceiveCount());

            if (MAX_RECEIVE_COUNT.equals(receiveCount)) {
                orquestrationGateway.sendResponse(
                        TransactionInformationStorage.getOrderId(),
                        customerId,
                        TransactionInformationStorage.getPaymentId(),
                        OrquestrationStepEnum.NOTIFY_CUSTOMER,
                        Boolean.FALSE
                );
            }

            throw new CustomerNotificationException(
                    ExceptionCodes.CUSTOMER_05_NOTIFICATION_FAILED,
                    String.format("Error creating payment. Error: %s", ex.getMessage())
            );
        }
    }

}
