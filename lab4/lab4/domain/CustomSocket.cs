using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;

namespace lab4.domain
{
    class CustomSocket
    {
        public Socket socket = null;

        public byte[] buffer = new byte[512];

        public StringBuilder response = new StringBuilder();

        public int id;

        public string hostname;

        public string endpoint;
    }
}
