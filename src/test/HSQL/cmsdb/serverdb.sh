java -Dproga=cmsdb   -Xms4096m -Xmx4096m -Dsun.rmi.dgc.client.gcInterval=3600000 -Dsun.rmi.dgc.server.gcInterval=3600000  -classpath ../cmsbo/lib/hsqldb.jar org.hsqldb.Server %1 %2 %3 %4 %5 %6 %7 %8 %9