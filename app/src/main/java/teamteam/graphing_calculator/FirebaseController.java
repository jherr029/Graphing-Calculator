package teamteam.graphing_calculator;

import android.util.Log;
import android.util.MonthDisplayHelper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Altros on 3/7/2018.
 */

public class FirebaseController {
    private static FirebaseController inst = new FirebaseController();

    private static final int MEM_MAX_SIZE = 5;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private DatabaseReference mRootref;
    private DatabaseReference memRef;
    private DatabaseReference funcRef;

    private ChildEventListener memCel;
    private ChildEventListener funcCel;

    private Map<String, MemoryValue> vals = new HashMap<>();
    private Map<String, MemoryFunction> funcs = new HashMap<>();

    private String oldestMemKey = null;
    private MemoryValue oldestMemVal = null;

    private boolean connected;

    private FirebaseController() {
        vals.clear();
        funcs.clear();
        connected = false;
        memCel = null;
        funcCel = null;
    }

    public static FirebaseController getInst() {
        return inst;
    }

    public Map<String, MemoryValue> getVals() {
        return vals;
    }

    public ArrayList<MemoryFunction> getFuncs() {
        Log.d("FirebaseController", "getFuncs: " + (new ArrayList<>(funcs.values())).toString());
        return new ArrayList<>(funcs.values());
    }

    public void disconnect() {
        connected = false;

        mAuth = null;
        mUser = null;

        vals.clear();
        funcs.clear();

        oldestMemKey = null;
        oldestMemVal = null;

        if (funcCel != null) {
            funcRef.removeEventListener(funcCel);
            funcCel = null;
        }

        if (memCel != null) {
            memRef.removeEventListener(memCel);
            memRef = null;
        }
    }

    public boolean Connected() {
        return connected;
    }

    public void pushMemoryValue(MemoryValue memoryValue) {
        if (!Connected()) {
            return;
        }

        String key = mRootref.child("Memory").child(mUser.getUid()).push().getKey();

        Map<String, Object> memVals = memoryValue.toMap();

        Map<String, Object> newChild = new HashMap<>();
        newChild.put("/Memory/" + mUser.getUid() + "/" + key, memVals);

        mRootref.updateChildren(newChild);
    }

    public void clearMemoryValues() {
        if (!Connected()) {
            return;
        }

        for (Map.Entry<String, MemoryValue> entry : vals.entrySet()) {
            memRef.child(entry.getKey()).removeValue();
        }
    }

    public void clearFunctions() {
        if (!Connected()) {
            return;
        }

        for (Map.Entry<String, MemoryFunction> entry : this.funcs.entrySet()) {
            funcRef.child(entry.getKey()).removeValue();
        }
    }

    public void pushFunctions(ArrayList<MemoryFunction> funcs) {
        if (!Connected()) {
            return;
        }

        clearFunctions();

        for (MemoryFunction func : funcs) {
            if (func.getComplete() == "") {
                continue;
            }

            String key = funcRef.push().getKey();

            Map<String, Object> funcVal = func.toMap();

            Map<String, Object> newChild = new HashMap<>();
            newChild.put("/" + key, funcVal);

            funcRef.updateChildren(newChild);
        }
    }

    private void findOldest() {
        oldestMemKey = null;
        oldestMemVal = null;

        for (Map.Entry<String, MemoryValue> entry : vals.entrySet()) {
            if (oldestMemKey == null || oldestMemVal.getTimestamp() > entry.getValue().getTimestamp()) {
                oldestMemKey = entry.getKey();
                oldestMemVal = entry.getValue();
            }
        }
    }

    public void connect() {
        if (Connected()) {
            return;
        }

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        if (mUser == null) {
            return;
        }
        connected = true;
        mRootref = FirebaseDatabase.getInstance().getReference();
        memRef = mRootref.child("Memory").child(mUser.getUid());

        memCel = memRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (!vals.containsKey(dataSnapshot.getKey())) {
                    vals.put(dataSnapshot.getKey(), dataSnapshot.getValue(MemoryValue.class));

                    if (oldestMemKey == null || oldestMemVal.getTimestamp() > dataSnapshot.getValue(MemoryValue.class).getTimestamp()) {
                        oldestMemKey = dataSnapshot.getKey();
                        oldestMemVal = dataSnapshot.getValue(MemoryValue.class);
                    }

                    if (vals.size() > MEM_MAX_SIZE) {
                        memRef.child(oldestMemKey).removeValue();

                        findOldest();
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                vals.remove(dataSnapshot.getKey());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                disconnect();
            }
        });

        funcRef = mRootref.child("Function").child(mUser.getUid());

        funcCel = funcRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("FirebaseController", "connect: funcCel: onChildAdded: new data...");
                if (!funcs.containsKey(dataSnapshot.getKey())) {
                    Log.d("FirebaseController", "connect: funcCel: onChildAdded: " + dataSnapshot.toString());
                    funcs.put(dataSnapshot.getKey(), dataSnapshot.getValue(MemoryFunction.class));
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                disconnect();
            }
        });
    }
}
