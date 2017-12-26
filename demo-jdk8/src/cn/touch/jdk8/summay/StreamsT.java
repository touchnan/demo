package cn.touch.jdk8.summay;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by touchnan on 2015/12/14.
 */
public class StreamsT {
    private enum Status {
        OPEN, CLOSED
    };

    private static final class Task {
        private final Status status;
        private final Integer points;

        Task( final Status status, final Integer points ) {
            this.status = status;
            this.points = points;
        }

        public Integer getPoints() {
            return points;
        }

        public Status getStatus() {
            return status;
        }

        @Override
        public String toString() {
            return String.format( "[%s, %d]", status, points );
        }


        public static void main(String[] args) throws IOException {
            final Collection< Task > tasks = Arrays.asList(
                    new Task(Status.OPEN, 5),
                    new Task(Status.OPEN, 13),
                    new Task(Status.CLOSED, 8)
            );

            final long totalPointsOfOpenTasks = tasks
                    .stream()
                    .filter( task -> task.getStatus() == Status.OPEN )
                    .mapToInt( Task::getPoints )
                    .sum();

            System.out.println( "Total points: " + totalPointsOfOpenTasks );


            final double totalPoints = tasks
                    .stream()
                    .parallel()
                    .map( task -> task.getPoints() ) // or map( Task::getPoints )
                    .reduce(0, Integer::sum);

            System.out.println( "Total points (all tasks): " + totalPoints );


            final double totalPoints1= tasks
                    .stream()
                    .parallel()
                    .map( task -> task.getPoints() ) // or map( Task::getPoints )
                    .reduce( 0, Integer::sum );

            System.out.println( "Total points (all tasks): " + totalPoints1 );


            final Collection< String > result = tasks
                    .stream()                                        // Stream< String >
                    .mapToInt( Task::getPoints )                     // IntStream
                    .asLongStream()                                  // LongStream
                    .mapToDouble( points -> points / totalPoints )   // DoubleStream
                    .boxed()                                         // Stream< Double >
                    .mapToLong( weigth -> ( long )( weigth * 100 ) ) // LongStream
                    .mapToObj( percentage -> percentage + "%" )      // Stream< String>
                    .collect(Collectors.toList());                 // List< String >

            System.out.println( result );

            String filename = "";
            final Path path = new File( filename ).toPath();
            try( Stream< String > lines = Files.lines(path, StandardCharsets.UTF_8) ) {
                lines.onClose( () -> System.out.println("Done!") ).forEach( System.out::println );
            }

        }
    }
}
