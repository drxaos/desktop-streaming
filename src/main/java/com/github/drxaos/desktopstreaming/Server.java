package com.github.drxaos.desktopstreaming;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.Router;
import org.apache.commons.io.IOUtils;

import java.awt.*;
import java.awt.event.InputEvent;

public class Server extends AbstractVerticle {

    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(new Server());
    }

    @Override
    public void start() throws Exception {
        Robot r = new Robot();
        Router router = Router.router(vertx);

        router.route("/").handler(ctx -> {
            try {
                ctx.response().putHeader("Content-Type", "text/html; charset=utf-8").end(
                        IOUtils.toString(this.getClass().getClassLoader().getResourceAsStream("index.html"))
                );
            } catch (Exception e) {
                e.printStackTrace();
                ctx.response().end("Error!");
            }
        });

        router.route("/scr").handler(ctx -> {
            try {
                final boolean[] ended = {false};
                ctx.response().setChunked(true).setWriteQueueMaxSize(1024);
                ctx.response().exceptionHandler(t -> {
                    ended[0] = true;
                });
                while (!ended[0]) {
                    ctx.response().write(Buffer.buffer(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0}));
                    Thread.sleep(1);
                }
            } catch (Exception e) {
                e.printStackTrace();
                ctx.response().end("Error!");
            }
        });

        router.route("/click").handler(ctx -> {
            try {
                r.mouseMove(Integer.parseInt(ctx.request().getParam("x")), Integer.parseInt(ctx.request().getParam("y")));
                r.mousePress(InputEvent.BUTTON1_MASK);
                r.mouseRelease(InputEvent.BUTTON1_MASK);
                ctx.response().end("ok");
            } catch (Exception e) {
                e.printStackTrace();
                ctx.response().end("Error!");
            }
        });

        vertx.createHttpServer().requestHandler(router::accept).listen(8888);
    }
}