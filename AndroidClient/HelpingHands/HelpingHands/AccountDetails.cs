using Android.Content;
using Android.Views;
using Android.Widget;
using HelpingHands.Adapters;
using HelpingHands.API;
using HelpingHands.Utils;
using System;
using System.Linq;
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
        TextView AccInterests;
        Button AccApplyForSponsorButton;

        protected void OnCreateAccountPage()
        {
            ProfileView = FindViewById<GridLayout>(Resource.Id.ProfileView);

            AccPrenume = FindViewById<TextView>(Resource.Id.AccPrenume);
            AccNume = FindViewById<TextView>(Resource.Id.AccNume);
            AccEmail = FindViewById<TextView>(Resource.Id.AccEmail);
            AccXpPct = FindViewById<TextView>(Resource.Id.AccXpPct);
            AccInterests = FindViewById<TextView> (Resource.Id.AccInterests);

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
                    var interestsList = await API.Client.GetVoluntarInterests(AppSession.UserId);
                    var interestsText = string.Join(", ", interestsList.Select(x => x.Name));

                    AccInterests.Text = interestsText;
                });
            }
            catch (Exception e)
            {
                await MessageBox.Alert(this, $"FAILED ACCOUNT PAGE\n\n{e.Message}", "Error");
            }
        }

        private void ApplyForSponsorButton_Click(object sender, EventArgs e)
        {
            Intent intent = new Intent(this, typeof(ApplySponsorshipActivity));
            StartActivity(intent);
        }
    }
}