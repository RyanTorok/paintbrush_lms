package server;

import server.database.QueryGate;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DHReq extends Command {
    public DHReq(String[] arguments) {
        super(arguments);
    }

    @Override
    String execute() throws WrongArgumentTypeException, SQLException {
        String targetNickname = getArgumentAsString(0);
        Long token = getArgumentAsLong(1);
        BigInteger publicAG;
        try {
            publicAG = new BigInteger(getArgumentAsString(2));
        } catch (NumberFormatException e) {
            return "error : illegal publicAG format";
        }
        long targetServerID = 0L;

        //get targetServerID from nickname
        ResultSet result = new QueryGate().query("SELECT `id` FROM registered_hosts WHERE nickname = ?;", "s", targetNickname);
        if (!result.isBeforeFirst())
            return "error : invalid server nickname";
        while (result.isBeforeFirst()) {
            result.next();
        }
        targetServerID = result.getLong("id");
        if (targetServerID == 0L) {
            return "error : host does not exist";
        }
        DHTable.Pair publicVars = DHTable.getPublicVars(Math.abs(token));
        Boolean argumentAsBoolean = getArgumentAsBoolean(3);
        String s = Boolean.toString(argumentAsBoolean);
        Poll.request(targetServerID, token * -1, getExecutorNickname(), "dhreq", publicVars.n, publicVars.g, publicAG, s);
        return "done";
    }
}
