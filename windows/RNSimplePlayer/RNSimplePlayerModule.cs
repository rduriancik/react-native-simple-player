using ReactNative.Bridge;
using System;
using System.Collections.Generic;
using Windows.ApplicationModel.Core;
using Windows.UI.Core;

namespace Simple.Player.RNSimplePlayer
{
    /// <summary>
    /// A module that allows JS to share data.
    /// </summary>
    class RNSimplePlayerModule : NativeModuleBase
    {
        /// <summary>
        /// Instantiates the <see cref="RNSimplePlayerModule"/>.
        /// </summary>
        internal RNSimplePlayerModule()
        {

        }

        /// <summary>
        /// The name of the native module.
        /// </summary>
        public override string Name
        {
            get
            {
                return "RNSimplePlayer";
            }
        }
    }
}
