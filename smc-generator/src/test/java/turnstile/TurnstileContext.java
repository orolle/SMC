
package turnstile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;
import rx.Observable;

public class TurnstileContext
    extends statemap.FSMContext
    implements java.io.Serializable
{
//---------------------------------------------------------------
// Member methods.
//

    public TurnstileContext(Turnstile owner)
    {
        this (owner, MainMap.Locked);
    }

    public TurnstileContext(Turnstile owner, TurnstileState initState)
    {
        super (initState);

        _owner = owner;
        _transitions = new TreeSet<>();

        _transitions.add("coin");
        _transitions.add("pass");
    }

    @Override
    public void enterStartState()
    {
        getState().entry(this);
        return;
    }

    public <T> Observable<T> coin(Double value)
    {
        return getState().<T>coin(this, value);
    }

    public <T> Observable<T> pass()
    {
        return getState().pass(this);
    }

    @FunctionalInterface
    public interface coin {
        <T> Observable<T> apply(Double value);
    }
    @FunctionalInterface
    public interface pass {
        <T> Observable<T> apply();
    }
    @FunctionalInterface
    public interface isEnoughValue {
        Observable<Boolean> apply(Double value);
    }
    public isEnoughValue isEnoughValue;
    @FunctionalInterface
    public interface activateAlarm {
        <T> Observable<T> apply();
    }
    public activateAlarm activateAlarm;
    @FunctionalInterface
    public interface deactivateAlarm {
        <T> Observable<T> apply();
    }
    public deactivateAlarm deactivateAlarm;
    @FunctionalInterface
    public interface alarm {
        <T> Observable<T> apply();
    }
    public alarm alarm;
    @FunctionalInterface
    public interface lock {
        <T> Observable<T> apply();
    }
    public lock lock;
    @FunctionalInterface
    public interface thankyou {
        <T> Observable<T> apply();
    }
    public thankyou thankyou;
    @FunctionalInterface
    public interface unlock {
        <T> Observable<T> apply();
    }
    public unlock unlock;
    public TurnstileState valueOf(int stateId)
        throws ArrayIndexOutOfBoundsException
    {
        return (_States[stateId]);
    }

    public TurnstileState getState()
        throws statemap.StateUndefinedException
    {
        if (_state == null)
        {
            throw(
                new statemap.StateUndefinedException());
        }

        return ((TurnstileState) _state);
    }

    protected Turnstile getOwner()
    {
        return (_owner);
    }

    public void setOwner(Turnstile owner)
    {
        if (owner == null)
        {
            throw (
                new NullPointerException(
                    "null owner"));
        }
        else
        {
            _owner = owner;
        }

        return;
    }

    public <T> Observable<T> rxClearState()    {
        super.clearState();
        return Observable.<T>empty();
    }

    public <T> Observable<T> rxSetState(statemap.State state)    {
        super.setState(state);
        return Observable.<T>empty();
    }

    public <T> Observable<T> rxPushState(statemap.State state)    {
        super.pushState(state);
        return Observable.<T>empty();
    }

    public <T> Observable<T> rxPopState()    {
        super.popState();
        return Observable.<T>empty();
    }

    public TurnstileState[] getStates()
    {
        return (_States);
    }

    public Set<String> getTransitions()
    {
        return (_transitions);
    }

    private void writeObject(java.io.ObjectOutputStream ostream)
        throws java.io.IOException
    {
        int size =
            (_stateStack == null ? 0 : _stateStack.size());
        int i;

        ostream.writeInt(size);

        for (i = 0; i < size; ++i)
        {
            ostream.writeInt(
                ((TurnstileState) _stateStack.get(i)).getId());
        }

        ostream.writeInt(_state.getId());

        return;
    }

    private void readObject(java.io.ObjectInputStream istream)
        throws java.io.IOException
    {
        int size;

        size = istream.readInt();

        if (size == 0)
        {
            _stateStack = null;
        }
        else
        {
            int i;

            _stateStack =
                new java.util.Stack<>();

            for (i = 0; i < size; ++i)
            {
                _stateStack.add(i, _States[istream.readInt()]);
            }
        }

        _state = _States[istream.readInt()];

        return;
    }

//---------------------------------------------------------------
// Member data.
//

    transient private Turnstile _owner;

    //-----------------------------------------------------------
    // Statics.
    //

    final Set<String> _transitions;
    transient private static TurnstileState[] _States =
    {
        MainMap.Locked,
        MainMap.Unlocked
    };

    //-----------------------------------------------------------
    // Constants.
    //

    private static final long serialVersionUID = 1L;

//---------------------------------------------------------------
// Inner classes.
//

    public static abstract class TurnstileState
        extends statemap.State
    {
    //-----------------------------------------------------------
    // Member methods.
    //

        public abstract Map<String, Integer> getTransitions();

        protected TurnstileState(String name, int id)
        {
            super (name, id);
        }

        protected <T> Observable<T> entry(TurnstileContext context) {return Observable.<T>empty();}
        protected <T> Observable<T> exit(TurnstileContext context) {return Observable.<T>empty();}

        protected <T> Observable<T> coin(TurnstileContext context, Double value)
        {
            return Default(context);
        }

        protected <T> Observable<T> pass(TurnstileContext context)
        {
            return Default(context);
        }

        protected <T> Observable<T> Default(TurnstileContext context)
        {
            return Observable.<T>error (
                new statemap.TransitionUndefinedException(
                    "State: " +
                    context.getState().getName() +
                    ", Transition: " +
                    context.getTransition()));
        }

    //-----------------------------------------------------------
    // Member data.
    //
    }

    /* package */ static abstract class MainMap
    {
    //-----------------------------------------------------------
    // Member methods.
    //

    //-----------------------------------------------------------
    // Member data.
    //

        //-------------------------------------------------------
        // Constants.
        //

        public static final MainMap_Locked Locked =
            new MainMap_Locked("MainMap.Locked", 0);
        public static final MainMap_Unlocked Unlocked =
            new MainMap_Unlocked("MainMap.Unlocked", 1);
    }

    protected static class MainMap_Default
        extends TurnstileState
    {
    //-----------------------------------------------------------
    // Member methods.
    //

        @Override
        public Map<String, Integer> getTransitions()
        {
            return (_transitions);
        }

        protected MainMap_Default(String name, int id)
        {
            super (name, id);
        }

    //-----------------------------------------------------------
    // Member data.
    //

        //---------------------------------------------------
        // Statics.
        //

        private static Map<String, Integer> _transitions;

        static
        {
            _transitions = new HashMap<>();
            _transitions.put("coin", statemap.State.TRANSITION_UNDEFINED);
            _transitions.put("pass", statemap.State.TRANSITION_UNDEFINED);
        }

        //---------------------------------------------------
        // Constants.
        //

        private static final long serialVersionUID = 1L;
    }

    private static final class MainMap_Locked
        extends MainMap_Default
    {
    //-------------------------------------------------------
    // Member methods.
    //

        @Override
        public Map<String, Integer> getTransitions()
        {
            return (_transitions);
        }

        private MainMap_Locked(String name, int id)
        {
            super (name, id);
        }

        @Override
        protected <T> Observable<T> entry(TurnstileContext context)
            {
                Turnstile owner = context.getOwner();

            return Observable.<T>concat(
Observable.<T>empty(),                context.activateAlarm.apply()
            );
        }

        @Override
        protected <T> Observable<T> exit(TurnstileContext context)
            {
            Turnstile owner = context.getOwner();

            return Observable.<T>concat(
Observable.<T>empty(),                context.deactivateAlarm.apply()
            );
        }

        @Override
        protected <T> Observable<T> coin(TurnstileContext context, Double value)
        {
            Turnstile owner = context.getOwner();
            List<Observable<T>> transitions = new ArrayList<>();
            AtomicBoolean transitionCompleted = new AtomicBoolean(false);
            Observable<T> transition = null;

            transition =
            context.isEnoughValue.apply(value).
            last().
            filter(b -> !transitionCompleted.get() && b).
            flatMap(v -> Observable.<T>concat(
                (context.getState()).exit(context),
                context.rxClearState(),
                context.unlock.apply(),
                context.rxSetState(MainMap.Unlocked),
                (context.getState()).entry(context),
                Observable.<T>empty()
                )).
            doOnCompleted(() -> transitionCompleted.set(true));
            transitions.add(transition);

            transition =
            context.isEnoughValue.apply(value).map(b -> !b).
            last().
            filter(b -> !transitionCompleted.get() && b).
            flatMap(v -> Observable.<T>concat(
                (context.getState()).exit(context),
                // No actions.
                context.rxSetState(MainMap.Locked),
                (context.getState()).entry(context),
                Observable.<T>empty()
                )).
            doOnCompleted(() -> transitionCompleted.set(true));
            transitions.add(transition);

            transition =
            Observable.just(Boolean.TRUE).
            last().
            filter(b -> !transitionCompleted.get() && b).
            flatMap(v -> 
                super.<T>coin(context, value)).
            doOnCompleted(() -> transitionCompleted.set(true));
            transitions.add(transition);
            return Observable.<T>concat(Observable.from(transitions));
        }

        @Override
        protected <T> Observable<T> pass(TurnstileContext context)
        {
            Turnstile owner = context.getOwner();
            List<Observable<T>> transitions = new ArrayList<>();
            AtomicBoolean transitionCompleted = new AtomicBoolean(false);
            Observable<T> transition = null;

            TurnstileState endState = context.getState();

            transition =
            Observable.just(Boolean.TRUE).
            last().
            filter(b -> !transitionCompleted.get() && b).
            flatMap(v -> Observable.<T>concat(
            context.rxClearState(),
            context.alarm.apply(),
            context.rxSetState(endState),
            Observable.<T>empty()
            )).
            doOnCompleted(() -> transitionCompleted.set(true));
            transitions.add(transition);

            return Observable.<T>concat(Observable.from(transitions));
        }

    //-------------------------------------------------------
    // Member data.
    //

        //---------------------------------------------------
        // Statics.
        //

        private static Map<String, Integer> _transitions;

        static
        {
            _transitions = new HashMap<>();
            _transitions.put("coin", statemap.State.TRANSITION_DEFINED_LOCALLY);
            _transitions.put("pass", statemap.State.TRANSITION_DEFINED_LOCALLY);
        }

        //---------------------------------------------------
        // Constants.
        //

        private static final long serialVersionUID = 1L;
    }

    private static final class MainMap_Unlocked
        extends MainMap_Default
    {
    //-------------------------------------------------------
    // Member methods.
    //

        @Override
        public Map<String, Integer> getTransitions()
        {
            return (_transitions);
        }

        private MainMap_Unlocked(String name, int id)
        {
            super (name, id);
        }

        @Override
        protected <T> Observable<T> coin(TurnstileContext context, Double value)
        {
            Turnstile owner = context.getOwner();
            List<Observable<T>> transitions = new ArrayList<>();
            AtomicBoolean transitionCompleted = new AtomicBoolean(false);
            Observable<T> transition = null;

            TurnstileState endState = context.getState();

            transition =
            Observable.just(Boolean.TRUE).
            last().
            filter(b -> !transitionCompleted.get() && b).
            flatMap(v -> Observable.<T>concat(
            context.rxClearState(),
            context.thankyou.apply(),
            context.rxSetState(endState),
            Observable.<T>empty()
            )).
            doOnCompleted(() -> transitionCompleted.set(true));
            transitions.add(transition);

            return Observable.<T>concat(Observable.from(transitions));
        }

        @Override
        protected <T> Observable<T> pass(TurnstileContext context)
        {
            Turnstile owner = context.getOwner();
            List<Observable<T>> transitions = new ArrayList<>();
            AtomicBoolean transitionCompleted = new AtomicBoolean(false);
            Observable<T> transition = null;

            transition =
            Observable.just(Boolean.TRUE).
            last().
            filter(b -> !transitionCompleted.get() && b).
            flatMap(v -> Observable.<T>concat(
            (context.getState()).exit(context),
            context.rxClearState(),
            context.lock.apply(),
            context.rxSetState(MainMap.Locked),
            (context.getState()).entry(context),
            Observable.<T>empty()
            )).
            doOnCompleted(() -> transitionCompleted.set(true));
            transitions.add(transition);

            return Observable.<T>concat(Observable.from(transitions));
        }

    //-------------------------------------------------------
    // Member data.
    //

        //---------------------------------------------------
        // Statics.
        //

        private static Map<String, Integer> _transitions;

        static
        {
            _transitions = new HashMap<>();
            _transitions.put("coin", statemap.State.TRANSITION_DEFINED_LOCALLY);
            _transitions.put("pass", statemap.State.TRANSITION_DEFINED_LOCALLY);
        }

        //---------------------------------------------------
        // Constants.
        //

        private static final long serialVersionUID = 1L;
    }
}
