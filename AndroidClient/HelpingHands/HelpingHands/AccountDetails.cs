using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using HelpingHands.Adapters;
using HelpingHands.API;
using HelpingHands.Utils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace HelpingHands
{
    public partial class MainVoluntarActivity
    {
        GridLayout ProfileView;

        TextView AccPrenume;
        TextView AccNume;
        TextView AccEmail;
        TextView AccXpPct;
        Button AccApplyForSponsorButton;
        Spinner AccInterestBox;

        InterestAdapter InterestAdapter;

        protected void OnCreateAccountPage()
        {
            ProfileView = FindViewById<GridLayout>(Resource.Id.ProfileView);

            AccPrenume = FindViewById<TextView>(Resource.Id.AccPrenume);
            AccNume = FindViewById<TextView>(Resource.Id.AccNume);
            AccEmail = FindViewById<TextView>(Resource.Id.AccEmail);
            AccXpPct = FindViewById<TextView>(Resource.Id.AccXpPct);
            AccInterestBox = FindViewById<Spinner>(Resource.Id.AccInterestBox);

            AccApplyForSponsorButton = FindViewById<Button>(Resource.Id.AccApplyForSponsorButton);

            AccApplyForSponsorButton.Click += ApplyForSponsorButton_Click;

            ProfileView.Visibility = ViewStates.Gone;

            Task.Run(Load);
        }

        async void Load()
        {
            try
            {
                RunOnUiThread(async () =>
                {
                    Console.WriteLine("Account Profile Details");
                    AccNume.Text =AppSession.UserData.User.Nume;
                    AccEmail.Text = AppSession.UserData.User.Email;
                    AccPrenume.Text = AppSession.UserData.User.Prenume;
                    AccXpPct.Text = AppSession.UserData.User.XpPoints.ToString();
                });
                var interestAdapter = new InterestAdapter(this, (await API.Client.GetVoluntarInterests(AppSession.UserId)).ToList());
                RunOnUiThread(() => AccInterestBox.Adapter = interestAdapter);
            }
            catch (Exception e)
            {
                await MessageBox.Alert(this, $"FAILED ACCOUNT PAGE\n\n{e.Message}", "Error");
            }
        }

        private void ApplyForSponsorButton_Click(object sender, EventArgs e)
        {
            throw new NotImplementedException();
        }
    }
}