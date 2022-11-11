/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supercars.rest;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import com.supercars.Leak;
import io.prometheus.client.Gauge;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tom.batchelor
 */
@Path("/leak")
public class LeakService {

    private final static Logger logger = Logger.getLogger(LeakService.class.getName());

    static final Gauge memUsage = Gauge.build().name("memory_usage").help("Total memory usage.").register();

    @Path("{number}/{size}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String leak(@PathParam("number") int number, @PathParam("size") int size) {
        logger.log(Level.FINE, "GET Increasing the leak size with {0} arrays of size {1} bytes", new Object[]{number, size});
        // TODO: Remove from load test so we can comment this back in
        //Leak.addToCollection(number, size);
        logger.log(Level.FINE, "Added to leak, size is now: {0}", Leak.getSize());
        memUsage.set(Runtime.getRuntime().totalMemory() + (Leak.getSize()*2));
        return Long.toString(Leak.getSize());
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getSize() {
        logger.fine("GET Getting leak size");
        return Long.toString(Leak.getSize());
    }

    @DELETE
    public void drainLeak() {
        logger.fine("DELETE Draining leak");
        Leak.drainCollection();
        logger.fine("Leak drained");
    }
}
