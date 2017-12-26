/*
 * cn.touch.cqrs.Cargo.java
 * Oct 30, 2013 
 */
package cn.touch.cqrs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Oct 30, 2013
 * 
 * @author <a href="mailto:touchnan@gmail.com">chegnqiang.han</a>
 * 
 */
public class Cargo {
    static List<ShipArrived> list = new ArrayList<ShipArrived>();
    private State currentState;

    public Cargo(List<ShipArrived> events) {
        for (ShipArrived event : events) {
            apply(event);
        }

    }

    public void arrive(Port port) {
        // logic to verify the action can be done
        // based on current state and command parameters
        // if (IsAlreadyInPort) throw Exception();

        // create an event of what happened with this action
        // it should not mutate state,
        // but it can capture external state when it arrives
        // it's also based on current state and command parameters
        ShipArrived event = new ShipArrived("id", port, new Date());
        // apply change du to the event
        // it should require only current state and
        apply(event);
        list.add(event);
        // events will be published to the rest of the system
        // from there.. This is where further side effect will
        // occure
    }

    private void apply(ShipArrived event) {
        // no decision should happen here !
        // currentState.Port = event.Port;
        // currentstate.LastMove = @event.Time;
    }

    interface State {

    };

    class Port {

    }

    class ShipArrived {

        public ShipArrived(String id, Port port, Date date) {

        }

    }
}
