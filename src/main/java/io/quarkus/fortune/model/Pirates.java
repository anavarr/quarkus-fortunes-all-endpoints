package io.quarkus.fortune.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Pirates {

    private Pirates() {
        // Avoid direct instantiation
    }

    public static List<String> QUOTES = Arrays.asList(
            "STOP BLOWING HOLES IN MY SHIP!!!",
            "Under a black flag we sail and the sea shall be our empire.",
            "May your anchor be tight, your cork be loose, your rum be spiced and your compass be true.",
            "If rum can’t fix it, you are not using enough rum.",
            "Be who you arrrr…",
            "May your blade always be wet, and powder dry.",
            "Loot is first and wimmen second. Because if ye have the first ye’ll have the second, but if ye have the second ye won’t have the first for long!",
            "I am a man of fortune and must seek my fortune.",
            "There is nothing like the smell of cannon fire in the morning.",
            "I’m sorry to see you here, but if you’d have fought like a man you needn’t hang like a dog.",
            "Always be yourself, unless you can be a pirate. Then always be a pirate.",
            "Work like a captain, play like a pirate.",
            "Keep calm and say ‘Arrr’.",
            "Life’s pretty good, and why wouldn’t it be? I’m a pirate, after all.",
            "You will always remember this as the day you almost caught Captain Jack Sparrow.",
            "Shiver me timbers.",
            "Pirate’s code: First freedom and the captain. Second the loot, third woman and the rum and at the end no mercy if they not immediately surrender!",
            "Damn ye, you are a sneaking puppy, and so are all those who will submit to be governed by laws which rich men have made for their own security.",
            "Damnation seize my soul if I give you quarters, or take any from you.",
            "Give me freedom or give me the rope. For I shall not take the shackles that subjugate the poor to uphold the rich.",
            "Yes, I do heartily repent. I repent I had not done more mischief; and that we did not cut the throats of them that took us, and I am extremely sorry that you aren’t hanged as well as we. ",
            "Piracy is the way o life. Ahoy.",
            "Why are pirates pirates? cuz they arrrrrr",
            "Sorry I Just Had To Put This One In. I’m Surprised Its Not Up There. After All Its Said By The Best Pirate In The World!",
            "Me I’m Dishonest. And A Dishonest Man You Can Always Trust To Be Dishonest. Honestly Its The Honest Ones You Want To Watch Out For Because You Never Know When They Are Going To Do Something Completely Stupid!",
            "A pirate is a man that is weak to achieve but too strong to steal from even the greatest achiever.",
            "The rougher the seas, the smoother we sail. Ahoy!",
            "Right from the Voyage og Noah, surviving was by sailing. Avast ye! and sail against the tides.",
            "A Pirate’s favorite movie is one that is rated “ARRRR”!",
            "To err is human but to arr is pirate!!",
            "There comes a time in most men’s lives where they feel the need to raise the Black Flag.",
            "Don’t ever; let people that see whats in your left hand, see whats in the right…",
            "Ahoy! lets trouble the water!",
            "My mom would not let me see the pirate movie because it was rated rrrrr.",
            "Avast ye landlubbers! Ye can throw ye lunch in Davy Jones’ locker, but not yer homework!",
            "If ye thinks he be ready to sail a beauty, ye better be willin’ to sink with her.",
            "Take what you can, give nothing back",
            "There is something I must tell you…I am not left- handed",
            "You can always trust the untrustworthy because you can always trust that they will be untrustworthy. Its the trustworthy you can’t trust.",
            "The Dutchman must have a Captain!",
            "Why is the rum gone?",
            "Why are pirates better than every one else?",
            "And that was done without a single drop of rum…",
            "How much does the pirate pay for an ear piercing? … A buccaneer! (buck- in- ear…)",
            "The Code is more like guidelines, really.",
            "Well actually piracy is a democracy with captains voted for by the crew. But I am touched by y’loyalty mate.",
            "Drink up me hearties yoho …a pirates life for me",
            "Piracy – Hostile take over. Without the messy paperwork."
    );

    public static List<Fortune> fortunes() {
        var fortunes = new ArrayList<Fortune>();
        int i = 0;
        for (String quote : Pirates.QUOTES) {
            fortunes.add(new Fortune(i, quote));
            i++;
        }
        return fortunes;
    };
}
