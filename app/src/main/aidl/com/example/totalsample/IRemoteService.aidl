package com.example.totalsample;
import com.example.totalsample.IRemoteServiceCallback;

interface IRemoteService {

    void registerCallback(IRemoteServiceCallback cb);

    void unregisterCallback(IRemoteServiceCallback cb);

}