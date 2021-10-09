package com.example.testtask;

public class UpdateMovieListCommand
{
    private final String query;
    private final IConnectToServer whatDo;
    private Thread r;

    public UpdateMovieListCommand(IConnectToServer whatDo, String query)
    {
        if(!query.isEmpty())
            this.query = query;
        else
            this.query = null;
        this.whatDo = whatDo;
        this.init();
    }

    public UpdateMovieListCommand(IConnectToServer whatDo)
    {
        this.query = null;
        this.whatDo = whatDo;
        this.init();
    }

    private void init(){
        if(this.query == null)
        {
            this.r = new RequestDataWithEmptyString(this.whatDo);
        }
        else
        {
            this.r = new RequestDataWithQuaery(this.whatDo, query);
        }
    }

    public void start()
    {
        this.r.start();
    }

    public boolean isAlive(){return this.r.isAlive();}

}
