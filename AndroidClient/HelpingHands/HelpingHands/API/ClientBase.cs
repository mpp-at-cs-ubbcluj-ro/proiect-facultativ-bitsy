using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Net.Http.Formatting;
using System.Text;
using System.Threading.Tasks;
using Xamarin.Essentials;

namespace HelpingHands.API
{
    internal class ClientBase
    {
        public static string BaseAddress =
            DeviceInfo.Platform == DevicePlatform.Android ? "http://192.168.43.243:8080" : "http://localhost:8080";        
        private static string URL_Base = BaseAddress + "/helpinghands";
        
        private HttpClient HttpClient = new HttpClient(new LoggingHandler(new HttpClientHandler()));

        public async void GetNoContent(string uri)
        {
            HttpResponseMessage response = await HttpClient.GetAsync(URL_Base + uri);
            if (!response.IsSuccessStatusCode)
                throw new RestException(await response.Content.ReadAsStringAsync());            
        }

        public async Task<T> Get<T>(string uri)
        {           
            HttpResponseMessage response = await HttpClient.GetAsync(URL_Base + uri);
            if (!response.IsSuccessStatusCode)
                throw new RestException(await response.Content.ReadAsStringAsync());
            return await response.Content.ReadAsAsync<T>();            
        }

        public async Task<T> Post<T>(string uri, object obj)
        {
            HttpResponseMessage response = await HttpClient.PostAsJsonAsync(URL_Base + uri, obj);
            if (!response.IsSuccessStatusCode)
                throw new RestException(await response.Content.ReadAsStringAsync());
            return await response.Content.ReadAsAsync<T>();            
        }

        public async Task<T> Put<T>(string uri, object obj)
        {
            HttpResponseMessage response = await HttpClient.PutAsJsonAsync(URL_Base + uri, obj);
            if (!response.IsSuccessStatusCode)                            
                throw new RestException(await response.Content.ReadAsStringAsync());
            return await response.Content.ReadAsAsync<T>();
        }

        public async void PutNoContent(string uri, object obj)
        {
            HttpResponseMessage response = await HttpClient.PutAsJsonAsync(URL_Base + uri, obj);
            if (!response.IsSuccessStatusCode)
                throw new RestException(await response.Content.ReadAsStringAsync());            
        }

        public async Task<T> Delete<T>(string uri)
        {
            HttpResponseMessage response = await HttpClient.DeleteAsync(URL_Base + uri);
            if (!response.IsSuccessStatusCode)
            {
                throw new ArgumentException(await response.Content.ReadAsStringAsync());
            }
            return await response.Content.ReadAsAsync<T>();
        }
    }
}