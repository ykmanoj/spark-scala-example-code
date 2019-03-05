Debugging spark applications

Set spark.executor.extraJavaOptions to include: “-XX:-PrintGCDetails -XX:+PrintGCTimeStamps” and look for long GC times on executor output

Use jmap to perform heap analysis:

jmap -histo [pid] to get a histogram of objects in the JVM heap
jmap -finalizerinfo [pid] to get a list of pending finalization objects (possible memory leaks)
Use jstack/ jconsole/ visualvm or other JVM profiling tool. Configure JVM arguments setting spark.executor.extraJavaOptions