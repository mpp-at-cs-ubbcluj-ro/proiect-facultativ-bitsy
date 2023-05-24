using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using HelpingHands.Data;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace HelpingHands.API
{
    internal static class Client
    {
        private static ClientBase ClientBase = new ClientBase();

        public static async Task<UserSession> Login(string username, string password)
            => await ClientBase.Post<UserSession>("/login", new LoginInfo(username, password));
        

        public static async Task<Eveniment[]> GetEvenimente()
            => await ClientBase.Get<Eveniment[]>("/evenimente");

        public static async Task<Eveniment[]> GetEvenimentePaged(int page, int perPage = 3)
            => await ClientBase.Get<Eveniment[]>($"/evenimente?page={page}&perPage={perPage}");

        public static async Task<Eveniment[]> GetEvenimenteByOrganizerId(int orgId)
            => await ClientBase.Get<Eveniment[]>($"/evenimente?volId={orgId}");

        public static async Task<Participant> AddVoluntarToEveniment(int eventId, int volId, string role)
            => await ClientBase.Put<Participant>($"/evenimente/{eventId}/participants", new RequestDataAddVoluntar(volId, role));

        public static async Task<Participant[]> GetParticipants(int eventId)
        {
            var p = await ClientBase.Get<Participant[]>($"/evenimente/{eventId}/participants");

            for (int i = 0; i < p.Length; i++)
                Console.WriteLine(p);

            return p;
        }


    }
}