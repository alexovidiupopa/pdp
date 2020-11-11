using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace lab4.utils
{
    class Parser
    {
        public static int PORT = 80; // http default port

        public static string GetRespBody(string responseContent)
        {
            // split response by removing new and empty lines
            var result = responseContent.Split(new[] { "\r\n\r\n" }, StringSplitOptions.RemoveEmptyEntries);
            if (result.Length > 1)
            {
                return result[1];
            }
            return "";
        }

        /// <summary>
        /// get request string
        /// </summary>
        /// <param name="hostname"> website </param>
        /// <param name="endpoint"> website nav specifics (after '/')</param>
        /// <returns> request string </returns>
        public static string GetRequestString(string hostname, string endpoint)
        {
            return "GET " + endpoint + " HTTP/1.1\r\n" +
                   "Host: " + hostname + "\r\n" + 
                   "Content-Length: 0\r\n\r\n";
        }

        /// <summary>
        /// get content length of response body
        /// </summary>
        /// <param name="respContent"> response message from server </param> 
        /// <returns></returns>
        public static int GetContentLen(string respContent)
        {
            var contentLen = 0;
            var respLines = respContent.Split('\r', '\n');
            foreach (string respLine in respLines)
            {
                var headDetails = respLine.Split(':');

                if (String.Compare(headDetails[0], "Content-Length", StringComparison.Ordinal) == 0)
                {
                    contentLen = int.Parse(headDetails[1]);
                }
            }

            return contentLen;
        }

        /// <summary>
        /// function to check if a header was obtained in the response
        /// </summary>
        /// <param name="responseContent"> response body from server </param>
        /// <returns></returns>
        public static bool ResponseHeaderObtained(string responseContent)
        {
            return responseContent.Contains("\r\n\r\n");
        }
    }
}
