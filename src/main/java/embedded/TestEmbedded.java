package embedded;

import java.io.IOException;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres;

import static ru.yandex.qatools.embed.postgresql.distribution.Version.Main.V9_6;
public class TestEmbedded {

    public static void main(String[] args) throws InterruptedException, IOException {
        MongodExecutable mongo = startMongo();

        Thread.sleep(2000);
        startPostgres();
    }


    private static void startPostgres() throws IOException {
        try (EmbeddedPostgres postgres = new EmbeddedPostgres(V9_6)) {
            final String url = postgres.start("localhost", 5432, "dbName", "userName", "password");
        }
    }

    private static MongodExecutable startMongo() throws IOException {
        MongodStarter starter = MongodStarter.getDefaultInstance();

        String bindIp = "localhost";
        int port = 12345;
        IMongodConfig mongodConfig = new MongodConfigBuilder()
                .version(Version.Main.PRODUCTION)

//
//                .cmdOptions(new MongoCmdOptionsBuilder()
//                        .defaultSyncDelay()
//                        .build())

                .net(new Net(bindIp, port, Network.localhostIsIPv6()))
                .build();

        MongodExecutable mongodExecutable = starter.prepare(mongodConfig);
        mongodExecutable.start();
        return mongodExecutable;
    }

}
