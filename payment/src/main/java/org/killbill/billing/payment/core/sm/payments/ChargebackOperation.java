/*
 * Copyright 2014 Groupon, Inc
 * Copyright 2014 The Billing Project, LLC
 *
 * The Billing Project licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.killbill.billing.payment.core.sm.payments;

import org.killbill.automaton.OperationResult;
import org.killbill.billing.payment.api.PaymentApiException;
import org.killbill.billing.payment.api.TransactionType;
import org.killbill.billing.payment.core.sm.PaymentAutomatonDAOHelper;
import org.killbill.billing.payment.core.sm.PaymentStateContext;
import org.killbill.billing.payment.dispatcher.PluginDispatcher;
import org.killbill.billing.payment.plugin.api.PaymentPluginApiException;
import org.killbill.billing.payment.plugin.api.PaymentPluginStatus;
import org.killbill.billing.payment.plugin.api.PaymentTransactionInfoPlugin;
import org.killbill.billing.payment.provider.DefaultNoOpPaymentInfoPlugin;
import org.killbill.billing.util.config.PaymentConfig;
import org.killbill.commons.locker.GlobalLocker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChargebackOperation extends PaymentOperation {

    private final Logger logger = LoggerFactory.getLogger(ChargebackOperation.class);

    public ChargebackOperation(final PaymentAutomatonDAOHelper daoHelper,
                               final GlobalLocker locker,
                               final PluginDispatcher<OperationResult> paymentPluginDispatcher,
                               final PaymentConfig paymentConfig,
                               final PaymentStateContext paymentStateContext) throws PaymentApiException {
        super(locker, daoHelper, paymentPluginDispatcher, paymentConfig, paymentStateContext);
    }

    @Override
    protected PaymentTransactionInfoPlugin doCallSpecificOperationCallback() throws PaymentPluginApiException {
        logger.debug("Starting CHARGEBACK for payment {} ({} {})", paymentStateContext.getPaymentId(), paymentStateContext.getAmount(), paymentStateContext.getCurrency());
        return new DefaultNoOpPaymentInfoPlugin(paymentStateContext.getPaymentId(),
                                                paymentStateContext.getTransactionId(),
                                                TransactionType.CHARGEBACK,
                                                paymentStateContext.getAmount(),
                                                paymentStateContext.getCurrency(),
                                                null,
                                                null,
                                                PaymentPluginStatus.PROCESSED,
                                                null,
                                                null);
    }
}
