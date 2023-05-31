using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Google.Android.Material.BottomNavigation;
using HelpingHands.Adapters;
using HelpingHands.API;
using HelpingHands.Data;
using HelpingHands.Utils;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using static Android.App.DatePickerDialog;

namespace HelpingHands
{
    [Activity(Label = "@string/app_name", Theme = "@style/AppTheme")]
    public class AccountDetails : Activity
    {
        TextView Prenume;
        TextView Nume;
        TextView Email;
        TextView XpPct;
        Button ApplyForSponsorButton;
        Spinner InterestBox;

        InterestAdapter InterestAdapter;

        protected override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            Xamarin.Essentials.Platform.Init(this, savedInstanceState);
            SetContentView(Resource.Layout.account_main);

            Prenume = FindViewById<TextView>(Resource.Id.Prenume);
            Nume = FindViewById<TextView>(Resource.Id.Nume);
            Email = FindViewById<TextView>(Resource.Id.Email);
            XpPct = FindViewById<TextView>(Resource.Id.XpPct);
            InterestBox = FindViewById<Spinner>(Resource.Id.InterestBox);

            ApplyForSponsorButton = FindViewById<Button>(Resource.Id.ApplyForSponsorButton);

            ApplyForSponsorButton.Click += ApplyForSponsorButton_Click;

            Task.Run(Load);
        }

        async void Load()
        {
            try
            {
                RunOnUiThread(async () =>
                {
                    Console.WriteLine("Account Profile Details");
                    Nume.Text = Nume.Text + " : " +  AppSession.UserData.User.Nume;
                    Email.Text = Email.Text + "     : " + AppSession.UserData.User.Email;
                    Prenume.Text = Prenume.Text + " : " + AppSession.UserData.User.Prenume;
                    XpPct.Text = XpPct.Text + "  : " + AppSession.UserData.User.XpPoints;
                });
                InterestAdapter = new InterestAdapter(this, (await API.Client.GetVoluntarInterests(AppSession.UserId)).ToList());
                RunOnUiThread(() => InterestBox.Adapter = InterestAdapter);
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