using lab4.domain;
using lab4.utils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;

namespace lab4.impl
{
    class TaskImpl
    {
        private static List<string> hostNames;

        public static void run(List<string> hostnames, bool async)
        {
            hostNames = hostnames;
            var tasks = new List<Task>();

            for (var i = 0; i < hostnames.Count; i++)
            {
                if (async)
                {
                    tasks.Add(Task.Factory.StartNew(DoStartAsync, i));
                }
                else
                {
                    tasks.Add(Task.Factory.StartNew(DoStart, i));
                }
            }

            Task.WaitAll(tasks.ToArray());
        }

        private static void DoStartAsync(object idObject)
        {
            var id = (int)idObject;

            StartAsyncClient(hostNames[id], id);
        }

        private static void DoStart(object idObject)
        {
            var id = (int)idObject;

            StartClient(hostNames[id], id);
        }

        private static void StartClient(string host, int id)
        {
            var ipHostInfo = Dns.GetHostEntry(host.Split('/')[0]); // get dns record
            var ipAddr = ipHostInfo.AddressList[0];
            var remEndPoint = new IPEndPoint(ipAddr, Parser.PORT); // basically stuff after "/" in the url

            var cl = new Socket(ipAddr.AddressFamily, SocketType.Stream, ProtocolType.Tcp);

            var state = new CustomSocket
            {
                sock = cl,
                hostname = host.Split('/')[0],
                endpoint = host.Contains("/") ? host.Substring(host.IndexOf("/", StringComparison.Ordinal)) : "/",
                remoteEndPoint = remEndPoint,
                id = id
            }; // state object

            ConnectWrapper(state).Wait(); // connect to remote server
            SendWrapper(state, Parser.GetRequestString(state.hostname, state.endpoint))
                .Wait(); // request data from server
            ReceiveWrapper(state).Wait(); // receive server response

            Console.WriteLine("Content length is:{0}", Parser.GetContentLen(state.responseContent.ToString()));
            
            // release the socket
            cl.Shutdown(SocketShutdown.Both);
            cl.Close();
        }

        private static async void StartAsyncClient(string host, int id)
        {
            var ipHostInfo = Dns.GetHostEntry(host.Split('/')[0]);
            var ipAddress = ipHostInfo.AddressList[0];
            var remoteEndpoint = new IPEndPoint(ipAddress, Parser.PORT);

            // create the TCP/IP socket
            var client =
                new Socket(ipAddress.AddressFamily, SocketType.Stream, ProtocolType.Tcp); // create client socket

            var state = new CustomSocket
            {
                sock = client,
                hostname = host.Split('/')[0],
                endpoint = host.Contains("/") ? host.Substring(host.IndexOf("/", StringComparison.Ordinal)) : "/",
                remoteEndPoint = remoteEndpoint,
                id = id
            }; // state object

            await ConnectAsyncWrapper(state); // connect to remote server

            await SendAsyncWrapper(state,
                Parser.GetRequestString(state.hostname, state.endpoint)); // request data from the server

            await ReceiveAsyncWrapper(state); // receive server response

            Console.WriteLine("Content length is:{0}", Parser.GetContentLen(state.responseContent.ToString()));

            // release the socket
            client.Shutdown(SocketShutdown.Both);
            client.Close();
        }

        private static async Task ConnectAsyncWrapper(CustomSocket state)
        {
            state.sock.BeginConnect(state.remoteEndPoint, ConnectCallback, state);

            await Task.FromResult<object>(state.connectDone.WaitOne());
        }

        private static Task ConnectWrapper(CustomSocket state)
        {
            state.sock.BeginConnect(state.remoteEndPoint, ConnectCallback, state);

            return Task.FromResult(state.connectDone.WaitOne());
        }

        private static void ConnectCallback(IAsyncResult ar)
        {
            // retrieve the details from the connection information wrapper
            var state = (CustomSocket)ar.AsyncState;
            var clientSocket = state.sock;
            var clientId = state.id;
            var hostname = state.hostname;

            clientSocket.EndConnect(ar); // complete connection

            Console.WriteLine("{0} --> Socket connected to {1} ({2})", clientId, hostname, clientSocket.RemoteEndPoint);

            state.connectDone.Set(); // signal connection is up
        }

        private static async Task SendAsyncWrapper(CustomSocket state, string data)
        {
            var byteData = Encoding.ASCII.GetBytes(data);

            // begin sending the data to the server  
            state.sock.BeginSend(byteData, 0, byteData.Length, 0, SendCallback, state);

            await Task.FromResult<object>(state.sendDone.WaitOne());
        }


        private static Task SendWrapper(CustomSocket state, string data)
        {
            // convert the string data to byte data using ASCII encoding.  
            var byteData = Encoding.ASCII.GetBytes(data);

            // begin sending the data to the server  
            state.sock.BeginSend(byteData, 0, byteData.Length, 0, SendCallback, state);

            return Task.FromResult(state.sendDone.WaitOne());
        }

        private static void SendCallback(IAsyncResult ar)
        {
            var state = (CustomSocket)ar.AsyncState;
            var clientSocket = state.sock;
            var clientId = state.id;

            var bytesSent = clientSocket.EndSend(ar); // complete sending the data to the server  

            Console.WriteLine("{0} --> Sent {1} bytes to server.", clientId, bytesSent);

            state.sendDone.Set(); // signal that all bytes have been sent
        }

        private static async Task ReceiveAsyncWrapper(CustomSocket state)
        {
            // begin receiving the data from the server
            state.sock.BeginReceive(state.buffer, 0, CustomSocket.BUFF_SIZE, 0, ReceiveCallback, state);

            await Task.FromResult<object>(state.receiveDone.WaitOne());
        }

        private static Task ReceiveWrapper(CustomSocket state)
        {
            // begin receiving the data from the server
            state.sock.BeginReceive(state.buffer, 0, CustomSocket.BUFF_SIZE, 0, ReceiveCallback, state);

            return Task.FromResult(state.receiveDone.WaitOne());
        }

        private static void ReceiveCallback(IAsyncResult ar)
        {
            // retrieve the details from the connection information wrapper
            var state = (CustomSocket)ar.AsyncState;
            var clientSocket = state.sock;

            try
            {
                // read data from the remote device.  
                var bytesRead = clientSocket.EndReceive(ar);

                // get from the buffer, a number of characters <= to the buffer size, and store it in the responseContent
                state.responseContent.Append(Encoding.ASCII.GetString(state.buffer, 0, bytesRead));

                // if the response header has not been fully obtained, get the next chunk of data
                if (!Parser.ResponseHeaderObtained(state.responseContent.ToString()))
                {
                    clientSocket.BeginReceive(state.buffer, 0, CustomSocket.BUFF_SIZE, 0, ReceiveCallback, state);
                }
                else
                {
                    foreach (var i in state.responseContent.ToString().Split('\r', '\n'))
                        Console.WriteLine(i);

                    state.receiveDone.Set(); // signal that all bytes have been received  
                    
                }
            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }

        }

    }
}
