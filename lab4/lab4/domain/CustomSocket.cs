using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace lab4.domain
{
    class CustomSocket
    {
        public Socket sock = null; // socket to connect to server

        public const int BUFF_SIZE = 512; // 512 bytes 

        public byte[] buffer = new byte[BUFF_SIZE]; // buffer

        public StringBuilder responseContent = new StringBuilder();

        public int id; // index of obj
        public string hostname; // website address
        public string endpoint; // website navigation specifics

        public IPEndPoint remoteEndPoint; // ip of endpoint website

        // events 
        public ManualResetEvent connectDone = new ManualResetEvent(false);
        public ManualResetEvent sendDone = new ManualResetEvent(false);
        public ManualResetEvent receiveDone = new ManualResetEvent(false);
    }
}
