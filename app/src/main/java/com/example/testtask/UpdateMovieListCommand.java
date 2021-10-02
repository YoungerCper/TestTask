package com.example.testtask;

public class UpdateMovieListCommand
{
    private final String query;
    private final IConnectToServer whatDo;

    public UpdateMovieListCommand(IConnectToServer whatDo, String query)
    {
        if(!query.isEmpty())
            this.query = query;
        else
            this.query = null;
        this.whatDo = whatDo;
    }

    public UpdateMovieListCommand(IConnectToServer whatDo)
    {
        this.query = null;
        this.whatDo = whatDo;
    }

    public void start()
    {
        if(this.query == null)
        {
            RequestDataWithEmptyString r = new RequestDataWithEmptyString(this.whatDo);
            r.start();
        }
        else
        {
            RequestDataWithQuaery r = new RequestDataWithQuaery(this.whatDo, query);
            r.start();
        }
    }

}
