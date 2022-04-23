/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supercars.externaldata;

import brave.Tracing;
import brave.jaxrs2.TracingClientFilter;
import com.supercars.Car;
import com.supercars.LoanQuote;
import com.supercars.LoanQuoteRequest;
import com.supercars.dataloader.CarDataLoader;
import com.supercars.tracing.TracingHelper;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author tombatchelor
 */
public class LoanQuotes {

    static Tracing tracing = TracingHelper.getTracing(TracingHelper.LOAN_NAME);

    private final static Logger logger = Logger.getLogger(LoanQuotes.class.getName());
    
    public static LoanQuote getQuote(LoanQuoteRequest quoteRequest) {
        logger.log(Level.FINE, "Getting loan quote request of ${0} over term of {1}", new Object[]{quoteRequest.getLoanAmount(), quoteRequest.getTerm()});
        LoanQuote loanQuote = null;
        try {
            loanQuote = getQuoteJerseySync(quoteRequest);
            logger.fine("Success getting loan quote request");
            logger.log(Level.FINE, "Loan quote of {0}% APR recieved, monthly payment of {1}", new Object[]{loanQuote.getRate(), loanQuote.getPayment()});
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }

        return loanQuote;
    }
    
    public static LoanQuote getQuote(int carID, int loanAmount, int term) {
        Car car = new CarDataLoader().getCar(carID);
        LoanQuoteRequest quoteRequest = new LoanQuoteRequest(car.getPrice(), loanAmount, term);
        return getQuote(quoteRequest);
    }

    private static LoanQuote getQuoteJerseySync(LoanQuoteRequest loanQuoteRequest) {
        logger.fine("Using sync HTTP call");
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://car-loan/carloan");
        target.register(TracingClientFilter.create(tracing));
        return target.request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(loanQuoteRequest, MediaType.APPLICATION_JSON), LoanQuote.class);
    }
}
